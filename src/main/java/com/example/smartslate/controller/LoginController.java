package com.example.smartslate.controller;

import com.example.smartslate.model.User;
import com.example.smartslate.repository.ILoginRepository;
import com.example.smartslate.repository.IUserRepository;
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

    @PostMapping("/login")
    public String processLoginForm(
            @RequestParam("usernameOrEmail") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        User user = iLoginRepository.findByUsernameOrEmailAndPassword(username, password);

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
