package com.shortener.shortenermvc.web;

import com.shortener.shortenermvc.service.Codec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/url")
public class ShortenerWriteController {

    // It is essentially Spring’s version of the Elvis operator (?:) — used for fallback/default values.
    @Value("${shortener.base-url:https://www.shortener.com}")
    private String baseUrl;

    private final Codec codec;

    // Return short URL
    @PostMapping
    public ResponseEntity<String> submitLongUrl(@RequestBody String longUrl) {
        log.debug("Requested encoding long URL = {}", longUrl);
        String shortUrl = codec.encode(longUrl);
        var response = baseUrl + "/" + shortUrl;
        return ResponseEntity.ok(response);
    }
}
