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
        model.addAttribute("user", newUser);
        model.addAttribute("userId", userId);
        model.addAttribute("createdAt", newUser.getCreatedAt());
        model.addAttribute("updatedAt", newUser.getUpdatedAt());
        model.addAttribute("username", newUser.getUserName());
        model.addAttribute("firstName", newUser.getFirstName());
        model.addAttribute("lastName", newUser.getLastName());
        model.addAttribute("email", newUser.getEmail());
        model.addAttribute("password", newUser.getPassword());
        model.addAttribute("phoneNumber", newUser.getPhoneNumber());
        model.addAttribute("role", newUser.getRole());
        return "create-user";
    }

    @GetMapping("/create")
    public String createProject(Model model) {
        Project newProject = new Project();
        model.addAttribute("newProject", newProject);
        return "Create project";
    }

    @PostMapping("/addProject")
    public String addProject(@ModelAttribute Project newProject, Model model) {
        int projectId = projectRepository.createProject(newProject);
        model.addAttribute("project", newProject);
        model.addAttribute("projectId", projectId);
        model.addAttribute("startDate", newProject.getStartDate());
        model.addAttribute("endDate", newProject.getEndDate());
        model.addAttribute("projectName", newProject.getProjectName());
        model.addAttribute("tasks", newProject.getTasks());
        model.addAttribute("description", newProject.getDescription());
        model.addAttribute("budget", newProject.getBudget());
        model.addAttribute("userId", newProject.getUserId());
        model.addAttribute("status", newProject.getStatus());
        return "create-Project";



    }
}
