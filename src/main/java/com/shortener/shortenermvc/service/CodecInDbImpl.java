package com.shortener.shortenermvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodecInDbImpl implements Codec {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final char[] BASE62_CHARS = BASE62.toCharArray();

    private static final SecureRandom random = new SecureRandom();

    private final Repository repository;

    @Override
    public String encode(String longUrl) {

        Optional<String> byLongUrl = repository.getByLongUrl(longUrl);
        if (byLongUrl.isPresent()) {
            return byLongUrl.get();
        }

        int baseLength = 6;
        int missesCounter = 0;
        String randomShort = null;
        boolean success = false;

        do {
            // For many collisions, increase the number of letters;
            if (missesCounter > 10) {
                baseLength++;
                missesCounter = 0;
            }
            randomShort = generate(baseLength);
            // Try to actually save
            try {
                repository.save(randomShort, longUrl);
                success = true;
            } catch (DuplicateKeyException ex) {
                log.warn("Failed to save shortUrl = {} to DB, ex", randomShort, ex);
                missesCounter++;
            }

        } while (!success);

        return randomShort;
    }

    @Override
    public Optional<String> decode(String shortUrl) {
        return repository.getByShortUrl(shortUrl);
    }

    public String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(BASE62_CHARS.length);
            sb.append(BASE62_CHARS[index]);
        }
        return sb.toString();
    }
}
