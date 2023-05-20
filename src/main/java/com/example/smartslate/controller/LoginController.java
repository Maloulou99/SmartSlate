package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.User;
import com.example.smartslate.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/")
@Controller
public class LoginController {
    private ILoginRepository iLoginRepository;
    private IUserRepository iUserRepository;
    private IProjectRepository iProjectRepository;
    private int current_userId;


    public LoginController(ILoginRepository iLoginRepository, IUserRepository iUserRepository, IProjectRepository iProjectRepository) {
        this.iLoginRepository = iLoginRepository;
        this.iUserRepository = iUserRepository;
        this.iProjectRepository = iProjectRepository;
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

    @PostMapping("/login")
    public String processLoginForm(@RequestParam("usernameOrEmail") String username, @RequestParam("password") String password, HttpSession session, Model model) {
        User user = iLoginRepository.findByUsernameOrEmailAndPassword(username, password);

        if (user != null) {
            int roleID = user.getRoleID();
            if (roleID == 1) {
                // Admin
                session.setAttribute("userId", user.getUserID());
                session.setMaxInactiveInterval(200);
                model.addAttribute("user", user);
                return "admin-page";
            } else if (roleID == 2) {
                List<Project> projects = iProjectRepository.getAllProjectsByUserId(user.getUserID());
                // Project Manager
                session.setAttribute("userId", user.getUserID());
                session.setMaxInactiveInterval(200);
                model.addAttribute("user", user);
                model.addAttribute("project", projects);
                model.addAttribute("roleName", iUserRepository.getRoleName(2));
                model.addAttribute("projects", iProjectRepository.getAllProjectsByUserId(user.getUserID()));
                return "pm-frontpage";
            } else if (roleID == 3) {
                // Employee
                session.setAttribute("userId", user.getUserID());
                session.setMaxInactiveInterval(200);
                model.addAttribute("user", user);
                return "employee-page";
            }
        }
        return "redirect:/login";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // invalidate session and return landing page
        session.invalidate();
        return "user-login";
    }
}
