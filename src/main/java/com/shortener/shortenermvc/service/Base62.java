package com.shortener.shortenermvc.service;

public interface Base62 {
    String encode(String original);
    String decode(String shortened);
}
