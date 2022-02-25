package com.kyukin.loginsuctomfilter1.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentAuthenticationToken implements Authentication {
// 학생의 통행증 역할

    // Authentication 을 상속 받음으로서 통행증 역할을 할 수 있다.

    private Student principal;  // 주요한
    private String credentials; // 신분증
    private String details;     // 세부사항
    private boolean authenticated;  // 인증 / 도장을 받을 장소

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return principal == null ? new HashSet<>() : principal.getRole();
    }

    @Override
    public String getName() {
        return principal == null ? "" : principal.getUsername();
    }
}
