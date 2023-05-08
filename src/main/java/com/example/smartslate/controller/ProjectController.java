package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public String getAllProjects(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        return "user-frontpage"; // navn på din Thymeleaf visningsside for projektlisten
    }

    @GetMapping("/create")
    public String createProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "create-project";
    }

    @PostMapping("/create")
    public String createProject(@ModelAttribute Project project) {
        projectService.createProject(project);
        return "redirect:/user-frontpage";
    }

    @GetMapping("/update/{id}")
    public String updateProjectForm(@PathVariable("id") int id, Model model) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        return "update-project";
    }

    @PostMapping("/update/{id}")
    public String updateProject(@ModelAttribute Project project, @PathVariable("id") int id) {
        project.setProjectId(id);
        projectService.updateProject(project);
        return "redirect:/user-frontpage";
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable("id") int id) {
        projectService.deleteProject(id);
        return "redirect:/user-frontpage";
    }
}
