package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;
import com.example.smartslate.service.ProjectService;
import com.example.smartslate.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
    public String addProject(@RequestParam int userId, @RequestParam String projectName, @RequestParam String description, @RequestParam LocalDate startDate,
                             @RequestParam LocalDate endDate, @RequestParam double budget, @RequestParam String status, @ModelAttribute("tasks") List<Task> tasks, Model model) {
        Project newProject = new Project();
        newProject.setUserId(userId);
        newProject.setProjectName(projectName);
        newProject.setDescription(description);
        newProject.setStartDate(startDate);
        newProject.setEndDate(endDate);
        newProject.setBudget(budget);
        newProject.setStatus(status);
        newProject.setTasks(tasks);

        int projectId = projectService.createProject(newProject);

        model.addAttribute("project", newProject);
        model.addAttribute("projectId", projectId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("name", projectName);
        model.addAttribute("description", description);
        model.addAttribute("budget", budget);
        model.addAttribute("userId", userId);
        model.addAttribute("status", status);

        return "create-project";
    }

}


