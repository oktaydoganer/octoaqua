package com.nexmind3.octoaqua.entity;

import com.nexmind3.octoaqua.enumeration.CompanyUserType;
import com.nexmind3.octoaqua.enumeration.State;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "company_user", uniqueConstraints={
        @UniqueConstraint(columnNames = {"user_id", "company", "type"})
})
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompanyUser extends BaseObject {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company", referencedColumnName = "id", nullable = false)
    private Company company;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", nullable = false)
    private CompanyUserType type;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private State state;


}
