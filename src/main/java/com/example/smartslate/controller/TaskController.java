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
        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/login";
        }
        int userId = userIdObj;
        User user = iUserRepository.getUser(userId);
        Project project = iProjectRepository.getProjectById(projectId);
        List<User> projectManagers = iUserRepository.getProjectManagersByProjectId(projectId);
        model.addAttribute("user", user);
        model.addAttribute("project", project);
        model.addAttribute("task", new Task());
        model.addAttribute("projectManagers", projectManagers);
        return "create-task";
    }
    @PostMapping("/tasks/create")
    public String createTask(@ModelAttribute Task task,
                             @RequestParam int projectId,
                             @RequestParam int projectManagerId,
                             HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        task.setProjectManagerID(projectManagerId);
        task.setProjectId(projectId);
        iTaskRepository.createTask(task, projectId, projectManagerId);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/projects/{projectId}/createTask")
    public String createTask(@PathVariable int projectId,
                             @RequestParam("taskName") String taskName,
                             @RequestParam("description") String description,
                             @RequestParam("deadline") String deadline,
                             @RequestParam("status") String status,
                             @RequestParam("projectManagerId") int projectManagerId,
                             Model model) {
        Project project = iProjectRepository.getProjectById(projectId);
        if (project != null) {
            Task newTask = new Task();
            newTask.setTaskName(taskName);
            newTask.setDescription(description);
            newTask.setDeadline(deadline);
            newTask.setStatus(status);
            iTaskRepository.createTask(newTask, projectId, projectManagerId);
            List<Task> tasks = iTaskRepository.getTasksByProjectId(projectId);
            model.addAttribute("tasks", tasks);
            model.addAttribute("newTask", new Task());
            model.addAttribute("projectManagers", iUserRepository.getProjectManagersByProjectId(projectId));
        }
        model.addAttribute("projectId", projectId); // add project ID to model
        return "create-task";
    }
    @GetMapping("/all-Tasks")
    public String getAllTasks(Model model) {
        List<Task> tasks = iTaskRepository.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "user-frontpage"; // navn på din Thymeleaf visningsside for projektlisten
    }

    @GetMapping("/task/{userId}")
    public String getUserTasks(@PathVariable("userId") int userId, Model model) {
        List<Task> userTasks = iTaskRepository.getTasksByProjectManagerID(userId);
        List<Project> userProjects = new ArrayList<>();
        for (Task task : userTasks) {
            Project project = iProjectRepository.getProjectById(task.getProjectId());
            userProjects.add(project);
        }
        model.addAttribute("tasks", userTasks);
        model.addAttribute("projects", userProjects);
        model.addAttribute("user", iUserRepository.getUser(userId));
        return "user-frontpage";
    }


    // Update task
    @GetMapping("/tasks/{id}/edit")
    public String editTask(Model model, @PathVariable("id") int taskId) {
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
    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable("id") int taskId, RedirectAttributes redirectAttributes) {
        int userId = iTaskRepository.getTaskById(taskId).getUserId();
        iTaskRepository.deleteTask(taskId);
        redirectAttributes.addAttribute("userId", userId);
        return "redirect:/user/{userId}";
    }






}
