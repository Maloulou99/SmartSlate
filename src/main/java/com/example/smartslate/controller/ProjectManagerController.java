package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;
import com.example.smartslate.repository.IProjectRepository;
import com.example.smartslate.repository.ITaskRepository;
import com.example.smartslate.repository.IUserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("smartslate")
@Controller
public class ProjectManagerController {
    private IUserRepository iUserRepository;
    private IProjectRepository iProjectRepository;
    private ITaskRepository iTaskRepository;

    public ProjectManagerController(IUserRepository iUserRepository, IProjectRepository iProjectRepository, ITaskRepository iTaskRepository) {
        this.iUserRepository = iUserRepository;
        this.iProjectRepository = iProjectRepository;
        this.iTaskRepository = iTaskRepository;
    }


    @GetMapping("/mainpage/{uid}")
    public String mainPage(@PathVariable int uid, Model model, HttpSession session) {
        User user = iUserRepository.getUser(uid);

        if (user == null) {
            // Hvis der ikke er nogen bruger med det angivne id, send brugeren til login-siden
            return "redirect:/";
        }

        model.addAttribute("userId", user.getUserID());
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());

        // Tjek om brugeren er logget ind
        if (session.getAttribute("loggedInUserId") != null && (int) session.getAttribute("loggedInUserId") == uid) {
            return "pm-frontpage"; // Hvis brugeren er logget ind, vis hovedsiden
        } else {
            return "redirect:/"; // Hvis brugeren ikke er logget ind, send brugeren til login-siden
        }
    }

    @GetMapping("/user/frontpage")
    public String getUserFrontsite(Model model, HttpSession session) {
        List<Project> projects = iProjectRepository.getAllProjects();
        Integer userId = (Integer) session.getAttribute("userId"); // Hent brugerens ID fra sessionen

        String roleName = iUserRepository.getRoleName(2);
        model.addAttribute("roleName", roleName);
        model.addAttribute("projects", projects);
        return "pm-frontpage";
    }







}


