package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.User;
import com.example.smartslate.service.LoginService;
import com.example.smartslate.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/")
@Controller
public class LoginController {
    private LoginService loginService;
    private UserService userService;
    private int current_userId;


    public LoginController(LoginService loginService, UserService userService) {
        this.loginService = loginService;
        this.userService = userService;
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
    public String processLoginForm(
            @RequestParam("usernameOrEmail") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        User user = loginService.findByUsernameOrEmailAndPassword(username, password);

        if (user == null) {
            // Brugeren findes ikke i databasen, vis en fejlmeddelelse
            model.addAttribute("errorMessage", "Invalid username/email or password.");
            return "user-login";
        }

        // Brugeren er logget ind, gem brugerens ID i sessionen
        session.setAttribute("userId", user.getUserID());
        session.setMaxInactiveInterval(200);

        // Redirect brugeren til "Min side"
        return "redirect:/smartslate/user/" + user.getUserID();
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // invalidate session and return landing page
        session.invalidate();
        return "user-login";
    }
}
