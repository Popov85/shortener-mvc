package com.shortener.shortenermvc.repository.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "original_urls", schema = "shortener")
public class OriginalUrl {

    public OriginalUrl() {
    }

    public OriginalUrl(byte[] originalUrlHash, String shortCode) {
        this.originalUrlHash = originalUrlHash;
        this.shortCode = shortCode;
        this.createdDateTime = LocalDateTime.now(); // Set to "now"
        this.expirationDateTime = this.createdDateTime.plusYears(1); // Expires after 1 year
    }

    @Id
    @Column(name = "original_url_hash", nullable = false)
    private byte[] originalUrlHash;

    @Column(name = "short_code", length = 12)
    private String shortCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Customer createdBy;

    @Column(name = "expiration_date_time")
    private LocalDateTime expirationDateTime;

    @Column(name = "created_date_time", nullable = false)
    private LocalDateTime createdDateTime;

}
