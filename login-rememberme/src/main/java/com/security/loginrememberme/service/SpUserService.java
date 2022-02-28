package com.security.loginrememberme.service;

import com.security.loginrememberme.domain.SpAuthority;
import com.security.loginrememberme.domain.SpUser;
import com.security.loginrememberme.repository.SpUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;

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

    public Optional<SpUser> findUser(String email) {
        return spUserRepository.findSpUserByEmail(email);
    }

    public SpUser save(SpUser spUser) {
        return spUserRepository.save(spUser);
    }

    public void addAuthority(Long userId, String authority) {
        spUserRepository.findById(userId).ifPresent(user->{
            SpAuthority newRole = new SpAuthority(user.getUserId(), authority);
            // user 에  authorities 가 비어 있으면
            if(user.getAuthorities() == null) {
                // HashSet<> : Set 의 구현체
                HashSet<SpAuthority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);
                save(user);
            // user 의 authorities 가 있지만, 저장하려는 authority 와 다를 때
            // ㄴ> authority 를 변경하고 싶을 때
            } else if (!user.getAuthorities().contains(newRole)) {
                HashSet<SpAuthority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities()); // 원래 권한 추가 (여러개 일 수도 있으므로 addAll() )
                authorities.add(newRole);   // 새 권한도 추가 (새권한은 하나니까 add() )
                user.setAuthorities(authorities);
                save(user);
            }
        });
    }
}
