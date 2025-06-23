package com.shortener.shortenermvc.service;

import com.shortener.shortenermvc.repository.OriginalUrlRepository;
import com.shortener.shortenermvc.repository.ShortCodeRepository;
import com.shortener.shortenermvc.repository.entity.ShortCode;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodecInDbImpl implements Codec {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final char[] BASE62_CHARS = BASE62.toCharArray();

    private static final SecureRandom random = new SecureRandom();

    private final ShortCodeRepository shortCodeRepository;

    private final OriginalUrlRepository originalUrlRepository;

    @Timed(value = "shortener-db.encode.execution", description = "Time taken by encode with DB")
    @Override
    public String encode(String longUrl) {

        var originalUrlHash = sha256(longUrl);

        Optional<ShortCode> byLongUrl = originalUrlRepository.findByOriginalUrlHash(originalUrlHash);
        if (byLongUrl.isPresent()) {
            return byLongUrl.map(shortCode -> shortCode.getOriginalUrl()).get();
        }

        int baseLength = 6;
        int missesCounter = 0;
        String randomShort = null;
        boolean success = false;

        do {
            // For many collisions, increase the number of letters;
            if (missesCounter > 3) {
                baseLength++;
                missesCounter = 0;
            }
            randomShort = generate(baseLength);
            // Try to actually save
            try {
                shortCodeRepository.save(new ShortCode(randomShort, longUrl, originalUrlHash));
                success = true;
            } catch (DuplicateKeyException ex) {
                log.warn("Failed to save shortUrl = {} to DB, ex", randomShort, ex);
                missesCounter++;
            }

        } while (!success);

        return randomShort;
    }

    @Override
    @Cacheable(value = "shortCodeCache", key = "#shortCode", unless = "#result == null")
    public String decode(String shortCode) {
        return shortCodeRepository.findByShortCode(shortCode)
                .map(url->url.getOriginalUrl())
                .orElse(null);
    }

    public String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(BASE62_CHARS.length);
            sb.append(BASE62_CHARS[index]);
        }
        return sb.toString();
    }

    public byte[] sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }
}
