package com.mts.mtsone.modules.auth.security;

import com.mts.mtsone.modules.auth.entity.RolePermission;
import com.mts.mtsone.modules.auth.entity.User;
import com.mts.mtsone.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameWithRoles(username)
            .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với username: " + username));

        List<GrantedAuthority> authorities = new ArrayList<>();

        user.getUserRoles().forEach(userRole -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getName()));
            // Lấy tất cả quyền của role hiện tại và thêm vào danh sách quyền
            List<RolePermission> rolePermissions = new ArrayList<>(userRole.getRole().getRolePermissions());
            rolePermissions.forEach(rolePermission -> {
                authorities.add(new SimpleGrantedAuthority(rolePermission.getPermission().getName()));
            });
        });

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .disabled(!user.isActive())
            .accountExpired(false)
            .credentialsExpired(false)
            .accountLocked(false)
            .authorities(authorities)
            .build();
    }
} 