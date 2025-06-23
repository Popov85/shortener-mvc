package com.shortener.shortenermvc.service.other;

import java.util.Optional;

public interface Storage {

    // Associate shortUrl with longURL
    // Associate longURL with shortUrl (to avoid overwrite)
    void save(String shortUrl, String longUrl);

    // Get longUrl based on shortUrl
    Optional<String> get(String shortUrl);
}
