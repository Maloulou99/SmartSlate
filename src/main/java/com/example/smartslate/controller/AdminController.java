package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;
import com.example.smartslate.repository.ILoginRepository;
import com.example.smartslate.repository.IProjectRepository;
import com.example.smartslate.repository.ITaskRepository;
import com.example.smartslate.repository.IUserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {
    private IUserRepository iUserRepository;
    private IProjectRepository iProjectRepository;
    private ITaskRepository iTaskRepository;
    private ILoginRepository iLoginRepository;

    public AdminController(IUserRepository iUserRepository, IProjectRepository iProjectRepository, ITaskRepository iTaskRepository, ILoginRepository iLoginRepository) {
        this.iUserRepository = iUserRepository;
        this.iProjectRepository = iProjectRepository;
        this.iTaskRepository = iTaskRepository;
        this.iLoginRepository = iLoginRepository;
    }

    @GetMapping("/")
    public String adminPage(Model model) {
        List<Project> projects = iProjectRepository.getAllProjects();
        model.addAttribute("projects", projects);
        return "admin-page";
    }


    @GetMapping("/user/{userId}")
    public String getUser(@PathVariable int userId, Model model) {
        User user = iUserRepository.getUser(userId);
        System.out.println(user);
        String roleName = iUserRepository.getRoleName(user.getRoleID()); // Henter rolle-navnet
        List<Project> projects = iProjectRepository.getAllProjectsByUserId(userId);
        List<Task> tasks = iTaskRepository.getTasksByProjectManagerID(user.getUserID());

        model.addAttribute("user", user);
        model.addAttribute("roleName", roleName); // Tilføjer rolle-navnet til model
        model.addAttribute("projects", projects);
        model.addAttribute("tasks", tasks);
        return "admin-page";
    }


    @GetMapping("/user/update/{userId}")
    public String updateUserForm(@PathVariable int userId, Model model, HttpSession httpSession) {
        Integer loggedUserId = (Integer) httpSession.getAttribute("userId");
        if (loggedUserId == null || loggedUserId != userId) {
            return "redirect:/login";
        }
        User user = iUserRepository.getUser(userId);
        model.addAttribute("user", user);
        return "user-update";
    }

    @PostMapping("/user/update")
    public String updateUser(@ModelAttribute User user, Model model) {
        iUserRepository.updateUser(user);
        iProjectRepository.getProjectById(user.getUserID());
        model.addAttribute("id", user.getUserID());
        return "redirect:/smartslate/user/" + user.getUserID();
    }


    @GetMapping("/deleteuser/{userId}")
    public String deleteUser(@PathVariable int userId) {
        iUserRepository.deleteUser(userId);
        return "redirect:/";
    }
}
