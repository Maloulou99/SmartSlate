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

import java.util.ArrayList;
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
        // Få bruger-id fra sessionen
        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/login";
        }
        int userId = userIdObj;

        // Få bruger og projekt
        User user = iUserRepository.getUser(userId);
        Project project = iProjectRepository.getProjectById(projectId);

        // Hent alle project managers med roleID = 2
        List<User> projectManagers = iUserRepository.getAllProjectManagersByID();

        // Tilføj data til modellen
        model.addAttribute("user", user);
        model.addAttribute("project", project);
        model.addAttribute("task", new Task());
        model.addAttribute("projectManagers", projectManagers);

        return "create-task";
    }


    @PostMapping("/projects/{projectId}/createTask")
    public String createTask(@PathVariable int projectId,
                             @ModelAttribute Task task,
                             Model model,
                             HttpSession session) {
        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/login";
        }
        int userId = userIdObj;
        iTaskRepository.createTask(task, projectId, task.getProjectManagerID());
        return "created-task";
    }


    @PostMapping("/tasks/create")
    public String createTaskFromParams(@RequestParam("taskName") String taskName,
                                       @RequestParam("description") String description,
                                       @RequestParam("deadline") String deadline,
                                       @RequestParam("status") String status,
                                       @RequestParam("projectManagerId") int projectManagerId,
                                       @RequestParam int projectId,
                                       HttpSession session) {
        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/login";
        }
        int userId = userIdObj;
        Task newTask = new Task(taskName, description, deadline, projectManagerId, status);
        iTaskRepository.createTask(newTask, projectId, projectManagerId);
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
        if (project.getProjectManagerId() != 0) {
            projectManager = iUserRepository.getUser(project.getProjectManagerId());
        }
        model.addAttribute("projectManager", projectManager);
        model.addAttribute("tasks", tasks);
        model.addAttribute("projectName", project.getProjectName());
        return "created-task";
    }





}



