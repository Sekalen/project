package com.example.final_project.config;

import com.example.final_project.entity.Role;
import com.example.final_project.entity.User;
import com.example.final_project.repository.RoleRepository;
import com.example.final_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // --- Создаём роли ---
        Role userRole = roleRepository.findByTitle("ROLE_USER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setTitle("ROLE_USER");
                    return roleRepository.save(role);
                });

        Role managerRole = roleRepository.findByTitle("ROLE_MANAGER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setTitle("ROLE_MANAGER");
                    return roleRepository.save(role);
                });

        Role adminRole = roleRepository.findByTitle("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setTitle("ROLE_ADMIN");
                    return roleRepository.save(role);
                });

        // --- Создаём пользователей ---
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.getRoles().add(userRole);
            userRepository.save(user);
            System.out.println("✅ Создан user: user / user123");
        }

        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.getRoles().add(adminRole);
            userRepository.save(admin);
            System.out.println("✅ Создан admin: admin / admin123");
        }

        if (userRepository.findByUsername("manager").isEmpty()) {
            User manager = new User();
            manager.setUsername("manager");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.getRoles().add(managerRole);
            userRepository.save(manager);
            System.out.println("✅ Создан manager: manager / manager123");
        }
    }
}