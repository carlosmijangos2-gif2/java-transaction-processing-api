package com.example.demo.transactionprocessing.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.transactionprocessing.entity.UserEntity;
import com.example.demo.transactionprocessing.repository.UserRepository;

@Component
public class UserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) {

        if (userRepository.findByUsername("admin").isEmpty()) {

            String passwordHash = passwordEncoder.encode("admin123");

            UserEntity user = new UserEntity(
                    "admin",
                    passwordHash
            );

            userRepository.save(user);
        }
    }
}