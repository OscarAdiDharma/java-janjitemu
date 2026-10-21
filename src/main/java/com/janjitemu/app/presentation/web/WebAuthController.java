package com.janjitemu.app.presentation.web;

import com.janjitemu.app.application.service.UserService;
import com.janjitemu.app.infrastructure.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebAuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal UserPrincipal user,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "registered", required = false) String registered,
                            Model model) {
        if (user != null) {
            return "admin".equalsIgnoreCase(user.getRole()) ? "redirect:/admin" : "redirect:/customer";
        }

        if (error != null) {
            model.addAttribute("errorMessage", "Email/Username atau password salah!");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Anda berhasil logout.");
        }
        if (registered != null) {
            model.addAttribute("successMessage", "Pendaftaran berhasil! Silakan login.");
        }

        return "login";
    }

    @GetMapping("/register")
    public String registerPage(@AuthenticationPrincipal UserPrincipal user, Model model) {
        if (user != null) {
            return "admin".equalsIgnoreCase(user.getRole()) ? "redirect:/admin" : "redirect:/customer";
        }
        return "register";
    }

    @PostMapping("/register-web")
    public String handleWebRegister(@RequestParam("username") String username,
                                    @RequestParam("email") String email,
                                    @RequestParam("password") String password,
                                    Model model) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            model.addAttribute("errorMessage", "Semua kolom wajib diisi!");
            return "register";
        }

        if (userService.existsByEmail(email.trim())) {
            model.addAttribute("errorMessage", "Email sudah terdaftar!");
            return "register";
        }

        if (userService.existsByUsername(username.trim())) {
            model.addAttribute("errorMessage", "Username sudah digunakan, pilih yang lain!");
            return "register";
        }

        userService.registerUser(username, email, password, "customer");

        return "redirect:/login?registered=true";
    }
}
