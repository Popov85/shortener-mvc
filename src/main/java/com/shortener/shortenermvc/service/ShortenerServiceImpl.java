package com.shortener.shortenermvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShortenerServiceImpl implements ShortenerService {

    private final Base62 base62;

    private final Counter counter;

    private final Storage storage;

    /**
     * 1. If already present, throw Exception
     * 2. If not present, increment counter, associate counter with this longUrl
     * 3. Produce Base62 representation of the counter;
     * 4. Save this representation as the key with value being original URL;
     * 5. Return this Base62 representation;
     * @param longUrl
     * @return shortUrl or throw Exception if longUrl is already present
     */
    @Override
    public String submitLongUrl(String longUrl) {
        String nextId = counter.getNextId();
        String shortUrl = base62.encode(nextId);
        storage.save(shortUrl, longUrl);
        return shortUrl;
    }

    /**
     * 1.If short URL is not present in our storage - return empty
     * 2. If present, get the value by the key
     * 3. Return this value (original long URL) wrapped into Optional
     * @param shortUrl
     * @return shortUrl or empty Optional
     */
    @Override
    public Optional<String> getShortUrl(String shortUrl) {
        return storage.get(shortUrl);
    }
}
