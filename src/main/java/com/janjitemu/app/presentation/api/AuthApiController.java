package com.janjitemu.app.presentation.api;

import com.janjitemu.app.application.dto.auth.AuthResponse;
import com.janjitemu.app.application.dto.auth.LoginRequest;
import com.janjitemu.app.application.dto.auth.RegisterRequest;
import com.janjitemu.app.application.dto.common.MessageResponse;
import com.janjitemu.app.application.service.UserService;
import com.janjitemu.app.domain.model.User;
import com.janjitemu.app.infrastructure.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            if (request.getEmail() == null || request.getPassword() == null || request.getUsername() == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Field tidak boleh kosong"));
            }

            if (userService.existsByEmail(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Email sudah terdaftar"));
            }

            userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword(), "customer");

            return ResponseEntity.ok(new MessageResponse("Register Berhasil"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error Server"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String loginIdentifier = null;
            if (request.getIdentifier() != null && !request.getIdentifier().isBlank()) {
                loginIdentifier = request.getIdentifier().trim();
            } else if (request.getEmail() != null && !request.getEmail().isBlank()) {
                loginIdentifier = request.getEmail().trim();
            } else if (request.getUsername() != null && !request.getUsername().isBlank()) {
                loginIdentifier = request.getUsername().trim();
            }

            if (loginIdentifier == null || request.getPassword() == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Identifier dan password wajib diisi"));
            }

            Optional<User> userOpt = userService.findByIdentifier(loginIdentifier);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("User tidak ditemukan"));
            }

            User user = userOpt.get();
            if (!userService.verifyPassword(request.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Password salah"));
            }

            String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());

            return ResponseEntity.ok(new AuthResponse(token, user.getRole(), user.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error Server"));
        }
    }
}
