package com.example.smartslate.controller;

import com.example.smartslate.model.User;
import com.example.smartslate.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/")
    public String landingPage(Model model, HttpSession session) {
        // Hvis brugeren er logget ind, redirect til user-frontsite
        if (isLoggedIn(session)) {
            return "redirect:/user-frontsite";
        }
        // Ellers vis loginformular
        model.addAttribute("loggedIn", false);
        return "index";
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
        return "redirect:/";
    }

    // Hjælpefunktion til at afgøre, om brugeren er logget ind
    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }
}
