package com.shortener.shortenermvc.service.other;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * If you need reversible encoding for binary data, stick to Base64.
 * Use Base62 for compact number-to-string conversions (e.g., URL shortening).
 */
@Service
public class Base62Impl implements Base62 {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final char[] BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    private static final int BASE_LENGTH = BASE62.length();

    private static final SecureRandom random = new SecureRandom();

    @Override
    public String encode(String original) {
        StringBuilder sb = new StringBuilder();
        long num = Long.parseLong(original);
        do {
            int remainder = (int)(num % BASE_LENGTH);
            char base62Char = BASE62.charAt(remainder);
            sb.append(base62Char);
            num /= BASE_LENGTH;
        } while (num > 0);
        return sb.reverse().toString();
    }

    @Override
    public String decode(String shortened) {
        long num = 0;
        for (char c : shortened.toCharArray()) {
            num = num * BASE_LENGTH + BASE62.indexOf(c);
        }
        return String.valueOf(num);
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
