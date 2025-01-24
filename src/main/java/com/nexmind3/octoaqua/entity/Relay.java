package com.nexmind3.octoaqua.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Relay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String ip_address;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = true) // Voltaj bilgisi isteğe bağlı
    private String voltage;  // Yeni alan
}
