package com.example.smartslate.controller;

import com.example.smartslate.model.Employee;
import com.example.smartslate.model.User;
import com.example.smartslate.repository.LoginRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/")
@Controller
public class LoginController {
    private LoginRepository loginRepository;
    private int currentUser;

    public LoginController(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    protected boolean isLoggedIn(HttpSession session, int uid) {
        return session.getAttribute("user") != null && currentUser == uid;
    }

    @GetMapping()
    public String landingPage() {
        return "index";
    }

    // User login
    @GetMapping("/user/login")
    public String showUserLogin() {
        return "user_login";
    }

    @PostMapping("/user/login")
    public String userLogin(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, Model model) {
        User user = loginRepository.checkUser(email, password);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            currentUser = user.getUserId();
            session.setMaxInactiveInterval(30);
            return "redirect:/wishlist/mainpage/" + currentUser;
        }
        model.addAttribute("wrongCredentials", true);
        return "user_login";
    }

    // Employee login
    @GetMapping("/employee/login")
    public String showEmployeeLogin() {
        return "employee_login";
    }

    @PostMapping("/employee/login")
    public String employeeLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, Model model) {
        Employee employee = loginRepository.checkEmployee(username, password);
        if (employee != null && employee.getPassword().equals(password)) {
            session.setAttribute("employee", employee);
            currentUser = employee.getEmployeeId();
            session.setMaxInactiveInterval(30);
            return "redirect:/employee/dashboard/" + currentUser;
        }
        model.addAttribute("wrongCredentials", true);
        return "employee_login";
    }


    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }
}

