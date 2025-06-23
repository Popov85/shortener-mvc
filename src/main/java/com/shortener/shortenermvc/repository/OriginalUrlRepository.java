package com.shortener.shortenermvc.repository;

import com.shortener.shortenermvc.repository.entity.OriginalUrl;
import com.shortener.shortenermvc.repository.entity.ShortCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OriginalUrlRepository extends JpaRepository<OriginalUrl, String> {

    Optional<ShortCode> findByOriginalUrlHash(byte[] originalUrlHash);
}
