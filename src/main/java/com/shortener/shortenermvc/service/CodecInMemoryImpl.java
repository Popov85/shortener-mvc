package com.shortener.shortenermvc.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Primary
public class CodecInMemoryImpl implements Codec {

    private static final Map<String, String> SHORT_TO_LONG_STORAGE = new ConcurrentHashMap<>();

    private static final Map<String, String> LONG_TO_SHORT_STORAGE = new ConcurrentHashMap<>();

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final char[] BASE62_CHARS = BASE62.toCharArray();

    private static final SecureRandom random = new SecureRandom();

    @Override
    public String encode(String longUrl) {
        if (LONG_TO_SHORT_STORAGE.containsKey(longUrl)) {
            return LONG_TO_SHORT_STORAGE.get(longUrl);
        }

        int baseLength = 6;
        int missesCounter = 0;
        String randomShort = null;

        do {
            randomShort = generate(baseLength);
            missesCounter++;
            // For many collisions, increase number of letters;
            if (missesCounter > 10) {
                baseLength++;
                missesCounter = 0;
            }
        } while (SHORT_TO_LONG_STORAGE.containsKey(randomShort));

        SHORT_TO_LONG_STORAGE.put(randomShort, longUrl);
        LONG_TO_SHORT_STORAGE.put(longUrl, randomShort);

        return randomShort;
    }

    @Override
    public Optional<String> decode(String shortUrl) {
        return Optional.ofNullable(SHORT_TO_LONG_STORAGE.get(shortUrl));
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
