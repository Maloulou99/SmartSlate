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

@RequestMapping("smartslate")
@Controller
public class TaskController {
    private ProjectService projectService;
    private TaskService taskService;

    public TaskController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }


    @PostMapping("/addTask")
    public String addTask(@RequestParam("description") String description, @RequestParam("deadline") Date deadline,
                          @RequestParam("assignedTo") String assignedTo, @RequestParam("status") String status,
                          @ModelAttribute("newProject") Project newProject, Model model) {

        Task newTask = new Task();
        newTask.setDescription(description);
        newTask.setDeadline(deadline);
        newTask.setAssignedTo(assignedTo);
        newTask.setStatus(status);

        ArrayList<Task> tasks = newProject.getTasks();
        tasks.add(newTask);
        newProject.setTasks(tasks);

        model.addAttribute("newTask", new Task()); // initialiser tomt Task-objekt til listen.
        model.addAttribute("tasks", tasks); // Tilføj opdateret liste over opgaver til listen.

        return "create-task";
    }

    @PostMapping("/projects/{projectId}/createTask")
    public String createTask(@PathVariable int projectId, @ModelAttribute Task task) {
        Project project = projectService.getProject(projectId);
        task.setProject(project);
        taskService.createTask(task);
        return "redirect:/projects/" + projectId;
    }
}
