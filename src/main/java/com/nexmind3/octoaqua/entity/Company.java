package com.nexmind3.octoaqua.entity;

import com.nexmind3.octoaqua.enumeration.State;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Company extends BaseObject {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private State state;

}
