package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.User;
import com.example.smartslate.service.ProjectService;
import com.example.smartslate.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("smartslate")
@Controller
public class SmartSlateController {
    private UserService userService;
    private ProjectService projectService;

    public SmartSlateController(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }

    @GetMapping("/create/user")
    public String createUser(Model model) {
        User newUser = new User();
        model.addAttribute("newUser", newUser);
        return "create-user";
    }

    @PostMapping("/adduser")
    public String addUser(@ModelAttribute User newUser, Model model) {
        int userId = userService.createUser(newUser);
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
        return "redirect:/user-frontsite";
    }

    @GetMapping("/user/{userId}")
    public String getUser(@PathVariable int userId, Model model) {
        User user = userService.getUser(userId);
        model.addAttribute("user", user);
        return "user-page";
    }


    @GetMapping("/create/project")
    public String createProject(Model model) {
        Project newProject = new Project();
        model.addAttribute("newProject", newProject);
        return "create-project";
    }

    @PostMapping("/addProject")
    public String addProject(@ModelAttribute Project newProject, Model model) {
        int projectId = projectService.createProject(newProject);
        model.addAttribute("project", newProject);
        model.addAttribute("projectId", projectId);
        model.addAttribute("startDate", newProject.getStartDate());
        model.addAttribute("endDate", newProject.getEndDate());
        model.addAttribute("name", newProject.getProjectName());
        model.addAttribute("description", newProject.getDescription());
        model.addAttribute("budget", newProject.getBudget());
        model.addAttribute("userId", newProject.getUserId());
        model.addAttribute("status", newProject.getStatus());
        return "create-project";
    }

}


