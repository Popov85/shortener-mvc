package com.shortener.shortenermvc.repository;

import com.shortener.shortenermvc.repository.entity.ShortCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortCodeRepository extends JpaRepository<ShortCode, String> {

        Optional<ShortCode> findByShortCode(String shortCode);

}

