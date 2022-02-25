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
    // Granted : 부여된
    // Authority : 권한
    // 권한은 중복이 없기 때문에 Set 을 활용하면 좋다.
    // Set<> : List<> 와 유사하지만 중복과 순서가 없다.
}
