package com.example.smartslate.controller;

import com.example.smartslate.model.User;
import com.example.smartslate.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    public boolean isLoggedIn(HttpSession session, int userId) {
        // Tjek om der er en bruger i sessionen
        User user = (User) session.getAttribute("user");
        if (user != null && user.getUserId() == userId) {
            return true; // Brugeren i sessionen matcher den angivne bruger-id
        } else {
            return false; // Brugeren i sessionen matcher ikke den angivne bruger-id
        }
    }


    @GetMapping("/login")
    public String showLoginForm() {
        return "user-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String usernameOrEmail, @RequestParam String password, HttpSession session, Model model) {
        User user = null;
        if (usernameOrEmail.contains("@")) {
            user = loginService.findByEmailAndPassword(usernameOrEmail, password);
        } else {
            user = loginService.findByUsernameAndPassword(usernameOrEmail, password);
        }

        if (user != null) {
            session.setAttribute("userId", user.getUserId());
            return "redirect:/user-frontsite";
        } else {
            model.addAttribute("error", "Invalid login credentials");
            return "user-login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user-login";
    }
}
