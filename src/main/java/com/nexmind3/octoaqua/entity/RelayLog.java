package com.nexmind3.octoaqua.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="relay_log")
@Data
public class RelayLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "relay_id", nullable = false) // Foreign key
    private Relay relay;

    @Column(nullable = false)
    private Boolean status; // Rölenin durumu (true = ON, false = OFF)

    @Column(nullable = false)
    private LocalDateTime actionTimestamp = LocalDateTime.now(); // Otomatik tarih

    @Column(nullable = false)
    private Boolean isManual = true; // Varsayılan: manuel müdahale (true)
}
