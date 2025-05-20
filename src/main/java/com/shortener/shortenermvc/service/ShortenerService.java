package com.shortener.shortenermvc.service;

import java.util.Optional;

/**
 * LeetCode 535: https://leetcode.com/problems/encode-and-decode-tinyurl/
 */
public interface ShortenerService {
    // Encodes a URL to a shortened URL.
    String submitLongUrl(String longUrl);
    // Decodes a shortened URL to its original URL.
    // If not found, return empty Optional
    Optional<String> getShortUrl(String shortUrl);
}
