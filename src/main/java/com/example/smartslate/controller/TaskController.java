package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import com.example.smartslate.service.ProjectService;
import com.example.smartslate.service.TaskService;
import com.example.smartslate.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping("smartslate")
@Controller
public class TaskController {
    private ProjectService projectService;
    private TaskService taskService;

    public TaskController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }


    @GetMapping("/projects/{projectId}/createTask")
    public String showCreateTaskForm(@PathVariable int projectId, Model model) {
        Project project = projectService.getProject(projectId);
        Task task = new Task();
        task.setProject(project);
        model.addAttribute("newTask", task);
        model.addAttribute("project", project);
        return "create-task";
    }

    @PostMapping("/projects/{projectId}/createTask")
    public String createTask(@PathVariable int projectId, @ModelAttribute Task newTask, Model model) {
        Project project = projectService.getProject(projectId);
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
        return "user-frontsite";
    }
}
