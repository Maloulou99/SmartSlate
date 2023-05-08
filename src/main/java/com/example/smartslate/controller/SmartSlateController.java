package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;
import com.example.smartslate.service.ProjectService;
import com.example.smartslate.service.TaskService;
import com.example.smartslate.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("smartslate")
@Controller
public class SmartSlateController {
    private UserService userService;
    private ProjectService projectService;
    private TaskService taskService;

    public SmartSlateController(UserService userService, ProjectService projectService, TaskService taskService) {
        this.userService = userService;
        this.projectService = projectService;
        this.taskService = taskService;
    }


    @GetMapping("/mainpage/{uid}")
    public String mainPage(@PathVariable int uid, Model model, HttpSession session) {
        User user = userService.getUser(uid);

        if (user == null) {
            // Hvis der ikke er nogen bruger med det angivne id, send brugeren til login-siden
            return "redirect:/";
        }

        model.addAttribute("userId", user.getUserID());
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());

        // Tjek om brugeren er logget ind
        if (session.getAttribute("loggedInUserId") != null && (int) session.getAttribute("loggedInUserId") == uid) {
            return "user-frontpage"; // Hvis brugeren er logget ind, vis hovedsiden
        } else {
            return "redirect:/"; // Hvis brugeren ikke er logget ind, send brugeren til login-siden
        }
    }

    @GetMapping("/user/frontpage")
    public String getUserFrontsite(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);

        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);

        return "user-frontpage";
    }





}


