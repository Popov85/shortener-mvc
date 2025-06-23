package com.shortener.shortenermvc.repository.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "short_codes", schema = "shortener")
public class ShortCode {

    public ShortCode() {
    }

    public ShortCode(String shortCode, String originalUrl, byte[] originalUrlHash) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.originalUrlHash = originalUrlHash;
        this.createdDateTime = LocalDateTime.now(); // Set to "now"
        this.expirationDateTime = this.createdDateTime.plusYears(1); // Expires after 1 year
    }

    @Id
    @Column(name = "short_code", length = 12)
    private String shortCode;

    @Column(name = "original_url", nullable = false, columnDefinition = "TEXT")
    private String originalUrl;

    @Column(name = "original_url_hash", nullable = false)
    private byte[] originalUrlHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Customer createdBy;

    @Column(name = "expiration_date_time")
    private LocalDateTime expirationDateTime;

    @Column(name = "created_date_time", nullable = false)
    private LocalDateTime createdDateTime;

}
