package com.shortener.shortenermvc.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "customer", schema = "shortener")
public class Customer {

    @Id
    private UUID id;

    @Column(nullable = false, length = 30)
    private String name;

}
