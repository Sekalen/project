package com.example.final_project.config;

import com.example.final_project.entity.Role;
import com.example.final_project.entity.User;
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
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ROLE_ADMIN);
            userRepository.save(admin);
            System.out.println("Admin created: admin / admin123");
        }

    
        if (userRepository.findByUsername("manager").isEmpty()) {
            User manager = new User();
            manager.setUsername("manager");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setRole(Role.ROLE_MANAGER);
            userRepository.save(manager);
            System.out.println("Manager created: manager / manager123");
        }
    }
}