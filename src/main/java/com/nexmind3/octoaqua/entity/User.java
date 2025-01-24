package com.nexmind3.octoaqua.entity;

import com.nexmind3.octoaqua.enumeration.State;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.*;

@Entity
@Table(name = "app_user")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User extends BaseObject {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "email", nullable = true, length = 512, unique = true)
    private String email;

    @Column(name = "legalIdentityNumber", nullable = true, length = 11, unique = true)
    private String legalIdentityNumber;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "account_lock")
    private boolean accountLock;

    @Column(name = "account_confirmed")
    private boolean accountConfirmed;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private State state;

    @Column(name = "last_login")
    private Date lastLogin;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) @Fetch(value = FetchMode.SELECT)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
