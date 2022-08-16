package com.study.jwtstudy.domain.user.domain;

import com.study.jwtstudy.domain.user.domain.type.Role;
import com.study.jwtstudy.global.entity.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Role role;
}
