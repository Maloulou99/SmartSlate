package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import com.example.smartslate.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("smartslate")
@Controller
public class ProjectController {
    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
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
                             @RequestParam LocalDate endDate, @RequestParam String budget, @RequestParam String status, Model model) {
        Project newProject = new Project();
        newProject.setUserId(userId);
        newProject.setProjectName(projectName);
        newProject.setDescription(description);
        newProject.setStartDate(startDate);
        newProject.setEndDate(endDate);
        newProject.setBudget(budget);
        newProject.setStatus(status);

        // Generate project ID automatically from the database
        int projectId = projectService.createProject(newProject);
        newProject.setProjectId(projectId);

        model.addAttribute("project", newProject);

        return "redirect:/user-frontsite/" + userId; // redirect to the user frontsite with the  // user id
    }

    @GetMapping("/delete-project")
    public String deleteProject(@RequestParam("id") int id, @RequestParam("userId") int userId) {
        projectService.deleteProject(id);
        return "reditect:/user-frontsite/" + userId; //will redirect to homepage after project is deleted.
    }

    @GetMapping("/update-project")
    public String updateProject(@RequestParam String projectName, @RequestParam String description, @RequestParam LocalDate startDate,
                                @RequestParam LocalDate endDate, @RequestParam String budget, @RequestParam String status, @ModelAttribute Project project, Model model) {

        Project updatedProject = new Project();
        updatedProject.setProjectName(projectName);
        updatedProject.setDescription(description);
        updatedProject.setStartDate(startDate);
        updatedProject.setEndDate(endDate);
        updatedProject.setBudget(budget);
        updatedProject.setStatus(status);

        //add updated data to project
        int projectId = projectService.createProject(updatedProject);
        updatedProject.setProjectId(projectId);
        return "redirect:/user-frontsite/";
    }

    @GetMapping("/projects")
    public String getAllProjects(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        return "user-frontsite"; // navn på din Thymeleaf visningsside for projektlisten
    }
}
