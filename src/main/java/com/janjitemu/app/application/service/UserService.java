package com.janjitemu.app.application.service;

import com.janjitemu.app.domain.model.User;
import com.janjitemu.app.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String email, String rawPassword, String role) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(username.trim(), email.trim(), encodedPassword, role);
        return userRepository.save(user);
    }

    public Optional<User> findByIdentifier(String identifier) {
        Optional<User> user = userRepository.findByEmail(identifier);
        if (user.isEmpty()) {
            user = userRepository.findByUsername(identifier);
        }
        return user;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
