package com.kyukin.loginsuctomfilter1.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student { // principal 이 될 객체

    private String id;
    private String username;
    private Set<GrantedAuthority> role;
}
