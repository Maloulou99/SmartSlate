package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.User;
import com.example.smartslate.repository.ProjectRepository;
import com.example.smartslate.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("smartslate")
@Controller
public class SmartSlateController {
    private UserRepository userRepository;
    private LoginController loginRepository;
    private ProjectRepository projectRepository;

    public SmartSlateController(UserRepository userRepository, LoginController loginRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.loginRepository = loginRepository;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/main-page/{uid}")

    public String mainPage(@PathVariable int uid, Model model, HttpSession session) {
        // Check if user is logged in
        if (!loginRepository.isLoggedIn(session, uid)) {
            return "redirect:/login";
        }

        User user = userRepository.getUser(uid);

        model.addAttribute("userId", user.getUserId());
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());

        List<Project> projects = projectRepository.getProjectsByUserId(uid);
        model.addAttribute("projects", projects);

        return "main-page";
    }


    @GetMapping("/create")
    public String createUser(Model model) {
        User newUser = new User();
        model.addAttribute("newUser", newUser);
        return "create-user";
    }

    @PostMapping("/adduser")
    public String addUser(@ModelAttribute User newUser, Model model) {
        int userId = userRepository.createUser(newUser);
        model.addAttribute("UserID", newUser.getUserId());
        model.addAttribute("firstName", newUser.getFirstName());
        model.addAttribute("lastName", newUser.getLastName());
        model.addAttribute("email", newUser.getEmail());
        model.addAttribute("password", newUser.getPassword());
        model.addAttribute("userId", userId);
        return "create-user";
    }

    // this.userId = userId;
    //        this.userName = userName;
    //        this.firstName = firstName;
    //        this.lastName = lastName;
    //        this.email = email;
    //        this.password = password;
    //        this.phoneNumber = phoneNumber;
    //        this.role = role;
    //        this.createdAt = createdAt;
    //        this.updatedAt = updatedAt;


}
