package com.janjitemu.app.presentation.web;

import com.janjitemu.app.infrastructure.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserPrincipal user) {
        if (user != null) {
            if ("admin".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin";
            } else {
                return "redirect:/customer";
            }
        }
        return "redirect:/login";
    }
}
