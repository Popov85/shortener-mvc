package com.shortener.shortenermvc.service;

import java.util.Optional;

public interface Repository {
    void save(String shortUrl, String longUrl);
    Optional<String> getByLongUrl(String longUrl);
    Optional<String> getByShortUrl(String shortUrl);
}
