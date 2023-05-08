package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;
import com.example.smartslate.repository.UserRepository;
import com.example.smartslate.service.ProjectService;
import com.example.smartslate.service.TaskService;
import com.example.smartslate.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("smartslate")
@Controller
public class UserController {
    private UserService userService;
    private ProjectService projectService;
    private TaskService taskService;

    public UserController(UserService userService, ProjectService projectService, TaskService taskService) {
        this.userService = userService;
        this.projectService = projectService;
        this.taskService = taskService;
    }


    @GetMapping("/create/user")
    public String createUser(Model model) {
        User newUser = new User();
        model.addAttribute("newUser", newUser);
        return "create-user";
    }

    @GetMapping("/user/{userId}")
    public String getUser(@PathVariable int userId, Model model) {
        User user = userService.getUser(userId);
        List<Project> projects = projectService.getProjectByUserId(userId);
        List<Task> tasks = taskService.getTasksByAssignedUser(user.getUserName());
        model.addAttribute("user", user);
        model.addAttribute("projects", projects);
        model.addAttribute("tasks", tasks);
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
    @PostMapping("/smartslate/user/update")
    public String updateUser(@ModelAttribute User updatedUser, Model model) {
        userService.updateUser(updatedUser);
        model.addAttribute("user", updatedUser);
        model.addAttribute("updatedAt", updatedUser.getUpdatedAt());
        model.addAttribute("username", updatedUser.getUserName());
        model.addAttribute("firstName", updatedUser.getFirstName());
        model.addAttribute("lastName", updatedUser.getLastName());
        model.addAttribute("email", updatedUser.getEmail());
        model.addAttribute("password", updatedUser.getPassword());
        model.addAttribute("phoneNumber", updatedUser.getPhoneNumber());
        model.addAttribute("role", updatedUser.getRole());
        return "user-frontsite";
    }
    @PostMapping("/deleteuser/{userId}")
    public String deleteUser(@PathVariable int userId, Model model) {
        userService.deleteUser(userId);
        return "user-deleted";
    }
    }









