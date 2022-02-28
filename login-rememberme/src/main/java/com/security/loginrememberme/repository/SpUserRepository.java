package com.security.loginrememberme.repository;

import com.security.loginrememberme.domain.SpUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpUserRepository extends JpaRepository<SpUser, Long> {

    Optional<SpUser> findSpUserByEmail(String email);
}
