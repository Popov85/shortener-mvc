package com.shortener.shortenermvc.service;

import java.util.Optional;

@org.springframework.stereotype.Repository
public class RepositoryImpl implements Repository {

    @Override
    public void save(String shortUrl, String longUrl) {
    }

    @Override
    public Optional<String> getByLongUrl(String longUrl) {
        return Optional.empty();
    }

    @Override
    public Optional<String> getByShortUrl(String shortUrl) {
        return Optional.empty();
    }

}
