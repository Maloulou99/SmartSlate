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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("")
@Controller
public class TaskController {
    private ProjectService projectService;
    private TaskService taskService;
    private UserService userService;

    public TaskController(ProjectService projectService, TaskService taskService, UserService userService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.userService = userService;
    }


    @GetMapping("/projects/{projectId}/createTask")
    public String showCreateTaskForm(@PathVariable int projectId, Model model) {
        Project project = projectService.getProjectById(projectId);
        Task task = new Task();
        task.setProject(project);
        model.addAttribute("newTask", task);
        model.addAttribute("project", project);
        return "create-task";
    }

    @PostMapping("/projects/{projectId}/createTask")
    public String createTask(@PathVariable int projectId, @ModelAttribute Task newTask, Model model) {
        Project project = projectService.getProjectById(projectId);
        newTask.setProject(project);
        taskService.createTask(newTask);
        List<Task> tasks = project.getTasks();
        model.addAttribute("newTask", new Task());
        model.addAttribute("tasks", tasks);
        return "create-task";
    }

    @PostMapping("/addTask")
    public String addTask(@ModelAttribute("newTask") Task newTask, @ModelAttribute("project") Project project, Model model) {
        List<Task> tasks = project.getTasks();
        newTask.setProject(project);
        taskService.createTask(newTask);
        tasks.add(newTask);
        model.addAttribute("newTask", new Task());
        model.addAttribute("tasks", tasks);
        return "user-frontpage";
    }


    // Update task
    @GetMapping("/tasks/{id}/edit")
    public String editTask(Model model, @PathVariable("id") int taskId) {
        Task task = taskService.getTaskById(taskId);
        model.addAttribute("task", task);
        model.addAttribute("users", userService.getAllUsers());
        return "update-task";
    }

    @PostMapping("/tasks/{id}/update")
    public String updateTask(@ModelAttribute("task") Task task, @PathVariable("id") int taskId) {
        task.setTaskId(taskId);
        taskService.updateTask(task);
        return "redirect:/tasks/" + taskId;
    }

    // Delete task
    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable("id") int taskId, RedirectAttributes redirectAttributes) {
        taskService.deleteTask(taskId);
        // overfør userId til redirect url
        redirectAttributes.addAttribute("userId", userService.getUser(taskId).getUserID());
        return "redirect:/user/{userId}";
    }


}
