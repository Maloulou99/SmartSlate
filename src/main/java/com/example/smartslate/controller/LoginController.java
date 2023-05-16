package com.example.smartslate.controller;

import com.example.smartslate.model.User;
import com.example.smartslate.repository.ILoginRepository;
import com.example.smartslate.repository.IUserRepository;
import com.example.smartslate.repository.LoginRepository;
import com.example.smartslate.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/")
@Controller
public class LoginController {
    private ILoginRepository iLoginRepository;
    private IUserRepository iUserRepository;
    private int current_userId;


    public LoginController(ILoginRepository iLoginRepository, IUserRepository iUserRepository) {
        this.iLoginRepository = iLoginRepository;
        this.iUserRepository = iUserRepository;
    }

    protected boolean isLoggedIn(HttpSession session, int uid) {
        return session.getAttribute("user") != null && current_userId == uid;
    }

    @GetMapping()
    public String landingPage() {
        return "index";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userId", null);
        return "user-login";
    }
/*
    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(required = false) int roleID) {
        User user = iLoginRepository.checkLogin(roleID);
        if (user != null) {
            int userRoleID = user.getRoleID();
            if (userRoleID == 1) {
                return "admin-page";
            } else if (userRoleID == 2 || userRoleID == 3) {
                return "user-login";
            }
        }
        model.addAttribute("userId", roleID);
        return "user-login";
    }*/

    @PostMapping("/login")
    public String processLoginForm(@RequestParam("usernameOrEmail") String username, @RequestParam("password") String password, HttpSession session, Model model) {
        User user = iLoginRepository.findByUsernameOrEmailAndPassword(username, password);

        if (user != null) {
            Integer roleID = user.getRoleID(); // Brug Integer i stedet for int for at kunne kontrollere for null-værdi

            if (roleID != null) {
                if (roleID == 1) {
                    // Admin
                    session.setAttribute("userId", user.getUserID());
                    session.setMaxInactiveInterval(200);
                    return "admin-page";
                } else if (roleID == 2) {
                    // Project Manager
                    session.setAttribute("userId", user.getUserID());
                    session.setMaxInactiveInterval(200);
                    return "user-frontpage";
                } else if (roleID == 3) {
                    // Employee
                    session.setAttribute("userId", user.getUserID());
                    session.setMaxInactiveInterval(200);
                    return "employee-page";
                }
            }
        }

        // Brugeren findes ikke i databasen eller rolID er null, vis en fejlmeddelelse
        model.addAttribute("errorMessage", "Invalid username/email or password.");
        model.addAttribute("user", user);
        return "user-login";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // invalidate session and return landing page
        session.invalidate();
        return "user-login";
    }
}
