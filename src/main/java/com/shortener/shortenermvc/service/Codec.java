package com.shortener.shortenermvc.service;

import java.util.Optional;

public interface Codec {
    String encode(String longUrl);

    Optional<String> decode(String shortUrl);
}
