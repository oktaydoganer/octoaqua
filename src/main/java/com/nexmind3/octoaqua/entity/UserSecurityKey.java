package com.nexmind3.octoaqua.entity;

import com.nexmind3.octoaqua.enumeration.SecurityKeyType;
import com.nexmind3.octoaqua.enumeration.State;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "user_security_key")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class UserSecurityKey extends BaseObject {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "random_key", length = 20, nullable = false)
    private String key;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiration_time")
    private Date expirationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userInfo", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private User user;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private State state;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "key_type")
    private SecurityKeyType securityKeyType;


}