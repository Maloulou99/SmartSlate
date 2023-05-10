package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;
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
    private LoginController loginController;

    public UserController(UserService userService, ProjectService projectService, TaskService taskService, LoginController loginController) {
        this.userService = userService;
        this.projectService = projectService;
        this.taskService = taskService;
        this.loginController = loginController;
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
        List<Project> projects = projectService.getAllProjectsByUserId(userId);
        List<Task> tasks = taskService.getTasksByAssignedUser(user.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("projects", projects);
        model.addAttribute("tasks", tasks);
        return "user-frontpage";
    }

    @PostMapping("/adduser")
    public String addUser(@ModelAttribute User newUser, Model model) {
        int userId = userService.createUser(newUser);
        newUser.setUserID(userId);
        model.addAttribute("user", newUser);
        model.addAttribute("createdAt", newUser.getCreatedAt());
        model.addAttribute("username", newUser.getUsername());
        model.addAttribute("firstName", newUser.getFirstName());
        model.addAttribute("lastName", newUser.getLastName());
        model.addAttribute("email", newUser.getEmail());
        model.addAttribute("password", newUser.getPassword());
        model.addAttribute("phoneNumber", newUser.getPhoneNumber());
        model.addAttribute("role", newUser.getRoleID());
        return "user-created";
    }

    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }
    @GetMapping("/user/update/{userId}")
    public String updateUserForm(@PathVariable int userId, Model model) {
        User user = userService.getUser(userId);
        model.addAttribute("user", user);
        return "user-update";
    }

    @PostMapping("/user/update")
    public String updateUser(@ModelAttribute("user") User updatedUser) {
        userService.updateUser(updatedUser);
        return "user-frontpage";
    }


    @GetMapping("/deleteuser/{userId}")
    public String deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return "user-frontpage";
    }

}









