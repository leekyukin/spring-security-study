package com.study.securitypractice.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "user_table")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public User(String email, String password, String name, Authority authority) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.authority = authority;
    }

}
