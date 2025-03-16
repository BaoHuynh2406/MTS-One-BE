package com.mts.mtsone.config;

import com.mts.mtsone.modules.auth.entity.Role;
import com.mts.mtsone.modules.auth.entity.User;
import com.mts.mtsone.modules.auth.entity.UserRole;
import com.mts.mtsone.modules.auth.repository.RoleRepository;
import com.mts.mtsone.modules.auth.repository.UserRepository;
import com.mts.mtsone.modules.auth.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            initRoles();
        }
        if (userRepository.count() == 0) {
            initAdminUser();
        }
    }

    private void initRoles() {
        log.info("Khởi tạo roles mặc định...");
        
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        adminRole.setDescription("Quản trị viên");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setName("USER");
        userRole.setDescription("Người dùng");
        roleRepository.save(userRole);

        log.info("Đã khởi tạo roles mặc định thành công!");
    }

    private void initAdminUser() {
        log.info("Khởi tạo tài khoản admin mặc định...");

        Role adminRole = roleRepository.findByName("ADMIN")
            .orElseThrow(() -> new RuntimeException("Không tìm thấy role ADMIN"));

        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("admin"));
        adminUser.setFullName("Administrator");
        adminUser.setEmail("admin@mtsone.com");
        adminUser.setActive(true);
        adminUser.setCreatedAt(new Date());
        adminUser = userRepository.save(adminUser);

        UserRole userRole = new UserRole();
        userRole.setUser(adminUser);
        userRole.setRole(adminRole);
        userRole.setAssignedAt(LocalDateTime.now());
        userRole.setAssignedBy("SYSTEM");
        userRoleRepository.save(userRole);

        log.info("Đã khởi tạo tài khoản admin mặc định thành công!");
    }
} 