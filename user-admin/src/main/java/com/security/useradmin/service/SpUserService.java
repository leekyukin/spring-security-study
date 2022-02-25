package com.security.useradmin.service;

import com.security.useradmin.repository.SpUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SpUserService implements UserDetailsService {

    private final SpUserRepository spUserRepository;

    public SpUserService(SpUserRepository spUserRepository) {
        this.spUserRepository = spUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return spUserRepository.findSpUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
