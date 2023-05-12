package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.User;
import com.example.smartslate.repository.IProjectRepository;
import com.example.smartslate.repository.IUserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private IProjectRepository iProjectRepository;
    private IUserRepository iUserRepository;

    public ProjectController(IProjectRepository iProjectRepository, IUserRepository iUserRepository) {
        this.iProjectRepository = iProjectRepository;
        this.iUserRepository = iUserRepository;
    }

    @GetMapping("/all-projects")
    public String getAllProjects(Model model) {
        List<Project> projects = iProjectRepository.getAllProjects();
        model.addAttribute("projects", projects);
        return "user-frontpage"; // navn på din Thymeleaf visningsside for projektlisten
    }

    @GetMapping("/create")
    public String createProjectForm(Model model, HttpSession session) {
        model.addAttribute("project", new Project());
        Integer userIdObj = (Integer) session.getAttribute("userId"); // Hent brugerens ID fra session
        if (userIdObj == null) {
            // Brugeren er ikke logget ind, så redirect til login siden eller vis en fejlmeddelelse
            return "redirect:/login";
        }
        int userId = userIdObj;
        User user = iUserRepository.getUser(userId); // Hent brugeren med det pågældende ID
        model.addAttribute("user", user);
        return "create-project";
    }


    @GetMapping("/projects/{userId}")
    public String getUserProjects(@PathVariable("userId") int userId, Model model) {
        List<Project> userProjects = iProjectRepository.getAllProjectsByUserId(userId);
        model.addAttribute("projects", userProjects);
        model.addAttribute("user", iUserRepository.getUser(userId));
        return "user-frontpage";
    }


    @PostMapping("/create")
    public String createProject(@ModelAttribute Project project, HttpSession httpSession) {
        int user = (int) httpSession.getAttribute("userId");
        project.setUserID(user);
        iProjectRepository.createProject(project);
        return "redirect:/smartslate/user/" + user;
    }

    @GetMapping("/update/{id}")
    public String updateProjectForm(@PathVariable("id") int id, Model model) {
        Project project = iProjectRepository.getProjectById(id);
        System.out.println(id);
        model.addAttribute("project", project);
        return "update-project";
    }

    @PostMapping("/update/{id}")
    public String updateProject(@ModelAttribute Project project, @PathVariable("id") int id) {
        project.setProjectId(id);
        iProjectRepository.updateProject(project);
        return "redirect:/user-frontpage";
    }


    @GetMapping("/delete/{id}/{userid}")
    public String deleteProject(@PathVariable("id") int id, @PathVariable("userid") int userID) {
        iProjectRepository.deleteProject(id);
        return "redirect:/smartslate/user/" + userID;
    }
}