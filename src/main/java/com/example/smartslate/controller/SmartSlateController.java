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
import java.util.ArrayList;
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

    @GetMapping("/user/{userId}")
    public String getUser(@PathVariable int userId, Model model) {
        User user = userService.getUser(userId);
        List<Project> projects = projectService.getProjectByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("projects", projects);
        return "user-frontsite";
    }


    @PostMapping("/adduser")
    public String addUser(@ModelAttribute User newUser, Model model) {
        int userId = userService.createUser(newUser);
        newUser.setUserId(userId);
        model.addAttribute("user", newUser);
        model.addAttribute("createdAt", newUser.getCreatedAt());
        model.addAttribute("username", newUser.getUserName());
        model.addAttribute("firstName", newUser.getFirstName());
        model.addAttribute("lastName", newUser.getLastName());
        model.addAttribute("email", newUser.getEmail());
        model.addAttribute("password", newUser.getPassword());
        model.addAttribute("phoneNumber", newUser.getPhoneNumber());
        model.addAttribute("role", newUser.getRole());
        return "user-created";
    }


    @GetMapping("/mainpage/{uid}")
    public String mainPage(@PathVariable int uid, Model model, HttpSession session) {
        User user = userService.getUser(uid);

        if (user == null) {
            // Hvis der ikke er nogen bruger med det angivne id, send brugeren til login-siden
            return "redirect:/";
        }

        model.addAttribute("userId", user.getUserId());
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());

        // Tjek om brugeren er logget ind
        if (session.getAttribute("loggedInUserId") != null && (int) session.getAttribute("loggedInUserId") == uid) {
            return "user-frontsite"; // Hvis brugeren er logget ind, vis hovedsiden
        } else {
            return "redirect:/"; // Hvis brugeren ikke er logget ind, send brugeren til login-siden
        }
    }


    @GetMapping("/create/project")
    public String createProject(Model model) {
        Project newProject = new Project();
        model.addAttribute("newProject", newProject);
        model.addAttribute("tasks", new ArrayList<Task>()); // add an empty ArrayList of tasks to the model
        return "create-project";
    }
    @PostMapping("/addProject")
    public String addProject(@RequestParam int userId, @RequestParam String projectName, @RequestParam String description, @RequestParam LocalDate startDate,
                             @RequestParam LocalDate endDate, @RequestParam double budget, @RequestParam String status, @ModelAttribute("newProject") Project newProject,
                             @ModelAttribute("tasks") List<Task> tasks, Model model) {
        newProject.setUserId(userId);
        newProject.setProjectName(projectName);
        newProject.setDescription(description);
        newProject.setStartDate(startDate);
        newProject.setEndDate(endDate);
        newProject.setBudget(budget);
        newProject.setStatus(status);
        newProject.setTasks(tasks);

        // Generate project ID automatically from the database
        int projectId = projectService.createProject(newProject);
        newProject.setProjectId(projectId);

        model.addAttribute("project", newProject);

        return "redirect:/user-frontsite/" + userId; // redirect to the user frontsite with the user id
    }



    @GetMapping("/projects")
    public String getAllProjects(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        return "user-frontsite"; // navn på din Thymeleaf visningsside for projektlisten
    }




}


