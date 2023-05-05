package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;
import com.example.smartslate.service.ProjectService;
import com.example.smartslate.service.UserService;
import jakarta.servlet.http.HttpSession;
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
    private LoginController loginController;

    public SmartSlateController(UserService userService, ProjectService projectService, LoginController loginController) {
        this.userService = userService;
        this.projectService = projectService;
        this.loginController = loginController;
    }

    @GetMapping("/mainpage/{uid}")
    public String mainPage(@PathVariable int uid, Model model, HttpSession session) {
        User user = userService.getUser(uid);

        if (user == null) {
            // Hvis der ikke er nogen bruger med det angivne id, send brugeren til login-siden
            return "redirect:/user-login";
        }

        model.addAttribute("userId", user.getUserId());
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());

        // Tjek om brugeren er logget ind
        if (loginController.isLoggedIn(session, uid)) {
            return "main-page"; // Hvis brugeren er logget ind, vis hovedsiden
        } else {
            return "redirect:/user-login"; // Hvis brugeren ikke er logget ind, send brugeren til login-siden
        }
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


