package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.User;
import com.example.smartslate.repository.IProjectRepository;
import com.example.smartslate.repository.ITaskRepository;
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
    private ITaskRepository iTaskRepository;

    public ProjectController(IProjectRepository iProjectRepository, IUserRepository iUserRepository, ITaskRepository iTaskRepository) {
        this.iProjectRepository = iProjectRepository;
        this.iUserRepository = iUserRepository;
        this.iTaskRepository = iTaskRepository;
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
    public String getUserProjects(@PathVariable("userId") int userId, Model model, HttpSession session) {
        Integer loggedInUserId = (Integer) session.getAttribute("userId");
        if (loggedInUserId == null || loggedInUserId != userId) {
            return "redirect:/login";
        }

        User user = iUserRepository.getUser(userId);
        String roleName = iUserRepository.getRoleName(user.getRoleID());

        List<Project> userProjects = iProjectRepository.getAllProjectsByUserId(userId);
        model.addAttribute("projects", userProjects);
        model.addAttribute("user", user);
        model.addAttribute("userId", userId);
        model.addAttribute("roleName", roleName);
        return "project-information";
    }


    @PostMapping("/create")
    public String createProject(@ModelAttribute Project project, HttpSession httpSession) {
        int user = (int) httpSession.getAttribute("userId");
        project.setUserID(user);
        iProjectRepository.createProject(project);
        return "redirect:/smartslate/user/" + user;
    }

    @GetMapping("/update/{id}")
    public String updateProjectForm(@PathVariable("id") int id, Model model, HttpSession session) {
        Integer userIdObj = (Integer) session.getAttribute("userId"); // Hent brugerens ID fra session
        if (userIdObj == null) {
            // Brugeren er ikke logget ind, så redirect til login siden eller vis en fejlmeddelelse
            return "redirect:/login";
        }
        Project project = iProjectRepository.getProjectById(id);
        model.addAttribute("project", project);

        // Get user information from HttpSession
        int userId = (int) session.getAttribute("userId");
        User user = iUserRepository.getUser(userId);
        String fullName = user.getFirstName() + " " + user.getLastName();
        model.addAttribute("userFullName", fullName);

        return "update-project";
    }

    @PostMapping("/update/{id}")
    public String updateProject(@ModelAttribute Project project, @PathVariable("id") int id, HttpSession session) {
        // Set project ID from path variable
        project.setProjectId(id);

        // Set user ID from HttpSession
        int userId = (int) session.getAttribute("userId");
        project.setUserID(userId);

        // Update project in database
        iProjectRepository.updateProject(project);

        // Redirect to user's project page
        return "redirect:/smartslate/user/" + userId;
    }



    @GetMapping("/delete/{projectId}/{userid}")
    public String deleteProject(@PathVariable("projectId") int taskId, @PathVariable("userid") int userID) {
        iProjectRepository.deleteProject(taskId);
        return "redirect:/smartslate/user/" + userID;
    }


}