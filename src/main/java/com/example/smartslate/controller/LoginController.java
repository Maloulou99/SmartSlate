package com.example.smartslate.controller;

import com.example.smartslate.model.User;
import com.example.smartslate.repository.LoginRepository;
import com.example.smartslate.repository.ProjectRepository;
import com.example.smartslate.repository.UserRepository;
import com.example.smartslate.service.LoginService;
import com.example.smartslate.service.ProjectService;
import com.example.smartslate.service.UserService;
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
    private LoginService loginService;
    private ProjectService projectService;
    private UserService userService;
    private int currentUser;

    public LoginController(LoginService loginService, ProjectService projectService, UserService userService) {
        this.loginService = loginService;
        this.projectService = projectService;
        this.userService = userService;
    }

    protected boolean isLoggedIn(HttpSession session, int uid) {
        return session.getAttribute("user") != null && currentUser == uid;
    }

    @GetMapping()
    public String landingPage(Model model) {
        model.addAttribute("loggedIn", true);
        return "index";
    }

    // User login
    @GetMapping("/user/login")
    public String showUserLogin() {
        return "user-login";
    }

    @PostMapping("/user/login")
    public String login(@RequestParam String usernameOrEmail, @RequestParam String password, HttpSession session) {
        User user = null;
        if (usernameOrEmail.contains("@")) {
            user = loginService.findByUsernameAndPassword(usernameOrEmail, password);
        } else {
            user = loginService.findByEmailAndPassword(usernameOrEmail, password);
        }
        if (user != null) {
            session.setAttribute("userId", user.getUserId());
            return "redirect:/main-page/" + user.getUserId();
        } else {
            return "user-login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        session.invalidate();
        model.addAttribute("loggedIn", false);
        return "index";
    }


}

