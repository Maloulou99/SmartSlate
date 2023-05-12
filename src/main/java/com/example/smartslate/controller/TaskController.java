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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("")
@Controller
public class TaskController {
    private IProjectRepository iProjectRepository;
    private ITaskRepository iTaskRepository;
    private IUserRepository iUserRepository;

    public TaskController(IProjectRepository iProjectRepository, ITaskRepository iTaskRepository, IUserRepository iUserRepository) {
        this.iProjectRepository = iProjectRepository;
        this.iTaskRepository = iTaskRepository;
        this.iUserRepository = iUserRepository;
    }
    @GetMapping("/projects/{projectId}/createTask")
    public String createTaskForm(@PathVariable int projectId, Model model, HttpSession session) {
        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/login";
        }

        List<User> projectManagers = iUserRepository.getProjectManagersByRoleId();
        User projectManager = iUserRepository.getProjectManagerByProjectId(projectId);
        Project project = iProjectRepository.getProjectById(projectId);
        List<Task> tasks = iTaskRepository.getTasksByProjectId(projectId);

        model.addAttribute("project", project);
        model.addAttribute("task", new Task());
        model.addAttribute("projectManagers", projectManagers);
        model.addAttribute("projectId", projectId);
        model.addAttribute("projectManagerId", projectManager);
        model.addAttribute("tasks", tasks);

        return "create-task";
    }

    @PostMapping("/projects/{projectId}/createTask")
    public String createTask(@PathVariable int projectId,
                             @ModelAttribute Task task,
                             @ModelAttribute Project project,
                             @RequestParam int projectManagerId,
                             Model model,
                             HttpSession session) {
        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/login";
        }
        int userId = userIdObj;

        // Opret opgave og hent dens ID
        int taskId = iTaskRepository.createTask(task.getTaskName(), task.getDescription(), task.getDeadline(), projectId, projectManagerId, task.getStatus());

        // Hent User objektet baseret på projectManagerId

        User projectManager = iUserRepository.getProjectManagerById(projectManagerId);

        // Sæt project manager ID på opgave-objektet
        task.setProjectManagerID(projectManagerId);
        project.setProjectId(projectManagerId);

        // Hent opgave objektet baseret på taskId
        Task createdTask = iTaskRepository.getTaskById(taskId);

        // Opdater opgavens projekt-id med den korrekte værdi
        createdTask.setProjectId(projectId);

        // Tilføj attributterne til modellen
        model.addAttribute("task", createdTask);
        model.addAttribute("projectManagerFirstName", projectManager.getFirstName());
        model.addAttribute("projectManagerLastName", projectManager.getLastName());

        return "created-task";
    }

    // Update task
    @GetMapping("/tasks/{id}/update")
    public String updateTask(Model model, @PathVariable("id") int taskId) {
        Task task = iTaskRepository.getTaskById(taskId);
        model.addAttribute("task", task);
        model.addAttribute("users", iUserRepository.getAllUsers());
        return "update-task";
    }


    @PostMapping("/tasks/{id}/update")
    public String updateTask(@ModelAttribute("task") Task task, @PathVariable("id") int taskId) {
        task.setTaskId(taskId);
        iTaskRepository.updateTask(task);
        return "redirect:/tasks/" + taskId;
    }


    // Delete task
    /*@PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable("id") int taskId, RedirectAttributes redirectAttributes) {
        int userId = iTaskRepository.getTaskById(taskId).getUserId();
        iTaskRepository.deleteTask(taskId);
        redirectAttributes.addAttribute("userId", userId);
        return "redirect:/user/{userId}";
    }*/
    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable("id") int taskId, RedirectAttributes redirectAttributes, HttpSession session) {
        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/login";
        }
        int userId = userIdObj;

        Task task = iTaskRepository.getTaskById(taskId);
        if (task == null) {
            // Task not found, redirect to user page
            redirectAttributes.addAttribute("userId", userId);
            return "redirect:/user/{userId}";
        }

        if (task.getProjectManagerID() != userId) {
            // User is not authorized to delete task, redirect to user page
            redirectAttributes.addAttribute("userId", userId);
            return "redirect:/user/{userId}";
        }

        // Delete task
        iTaskRepository.deleteTask(taskId);

        redirectAttributes.addAttribute("userId", userId);
        return "redirect:/user/{userId}";
    }

    @GetMapping("/projects/{projectId}/tasks")
    public String getTasksByProjectId(@PathVariable int projectId, Model model, HttpSession session) {
        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/login";
        }
        int userId = userIdObj;
        List<Task> tasks = iTaskRepository.getTasksByProjectId(projectId);
        Project project = iProjectRepository.getProjectById(projectId);
        User projectManager = null;
        if (project.getUserID() != 0) {
            projectManager = iUserRepository.getUser(project.getUserID());
        }
        model.addAttribute("projectManager", projectManager);
        model.addAttribute("tasks", tasks);
        model.addAttribute("projectName", project.getProjectName());
        return "created-task";
    }


    @GetMapping("/projects/tasks/{projectId}/{userId}")
    public String showTasksForProject(@PathVariable int projectId, @PathVariable int userId, Model model) {
        // code to fetch tasks for the given projectId from the database
        List<Task> tasks = iTaskRepository.getTasksByProjectManagerID(projectId);
        // get the userId of the user who created the project
        userId = iUserRepository.getUserIdByProjectId(projectId);
        // add tasks and projectId to the model
        model.addAttribute("tasks", tasks);
        model.addAttribute("projectId", projectId);
        model.addAttribute("userId", userId);

        // redirect to the user-frontpage with the user id as a path variable
        return "redirect:/smartslate/user/" + userId;
    }





}



