package com.shortener.shortenermvc.service.other;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class StorageImpl implements Storage {

    private final Map<String, String> shortUrlStorage = new ConcurrentHashMap<>();

    private final Map<String, String> longUrlStorage = new ConcurrentHashMap<>();

    // Transactional for RDBMS
    @Override
    public void save(String shortUrl, String longUrl) {
        String existingLongUrl = longUrlStorage.get(longUrl);
        if (existingLongUrl != null) {
            // User expectation: When I share a short URL,
            // I expect it to never change or point somewhere else later.
            throw new RuntimeException("Such URL is already present");
        }
        shortUrlStorage.put(shortUrl, longUrl);
        longUrlStorage.put(longUrl, shortUrl);
    }

    @Override
    public Optional<String> get(String shortUrl) {
        return Optional.ofNullable(shortUrlStorage.get(shortUrl));
    }
}
