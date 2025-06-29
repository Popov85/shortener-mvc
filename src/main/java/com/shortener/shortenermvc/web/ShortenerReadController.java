package com.shortener.shortenermvc.web;

import com.shortener.shortenermvc.service.Codec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/r")
public class ShortenerReadController {

    private final Codec codec;

    // Return redirect to the long URL
    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> getRedirected(@PathVariable String shortUrl) {
        log.debug("Requested the original long URL, short = {}", shortUrl);
        var longUrl = codec.decode(shortUrl);

        if (longUrl==null) {
            throw new RuntimeException("Not found!");
        }

        log.debug("Found long URL = {}", longUrl);
        // The browser automatically follows the Location header in a 3xx response (302, 301, 307, etc.).
        return ResponseEntity
                .status(302)
                .header("Location", longUrl)
                .build();
    }
}
