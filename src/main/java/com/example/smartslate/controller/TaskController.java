package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;
import com.example.smartslate.service.ProjectService;
import com.example.smartslate.service.TaskService;
import com.example.smartslate.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
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

    @GetMapping("/all-Tasks")
    public String getAllTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "user-frontpage"; // navn på din Thymeleaf visningsside for projektlisten
    }

    @GetMapping("/projects/{projectId}/createTask")
    public String createTaskForm(Model model, HttpSession session) {
        model.addAttribute("task", new Project());
        Integer userIdObj = (Integer) session.getAttribute("userId"); // Hent brugerens ID fra session
        if (userIdObj == null) {
            // Brugeren er ikke logget ind, så redirect til login siden eller vis en fejlmeddelelse
            return "redirect:/login";
        }
        int userId = userIdObj;
        User user = userService.getUser(userId); // Hent brugeren med det pågældende ID
        model.addAttribute("user", user);
        return "create-task";
    }

   /* @PostMapping("/projects/{projectId}/createTask")
    public String createTask(@RequestParam("projectId") int projectId,
                             @RequestParam("assignedTo") String assignedTo,
                             @RequestParam("description") String description,
                             @RequestParam("deadline") String deadline,
                             @RequestParam("status") String status, Model model) {
        Project project = projectService.getProjectById(projectId);
        if (project != null) {
            Task newTask = new Task();
            newTask.setProjectId(projectId);
            newTask.setDescription(description);
            newTask.setDeadline(deadline);
            newTask.setStatus(status);
            newTask.setProject(project);
            taskService.createTask(newTask);
            List<Task> tasks = taskService.getTasksByProjectId(projectId);
            model.addAttribute("tasks", tasks);
            model.addAttribute("newTask", new Task());
            model.addAttribute("projectManagers", userService.getProjectManagersByProjectId(projectId));
        }
        model.addAttribute("projectId", projectId); // add project ID to model
        return "create-task";
    }*/
   @PostMapping("/projects/{projectId}/createTask")
   public String createTask(@RequestParam("projectId") int projectId, @RequestParam("taskId") int taskId, Model model) {
       Project project = projectService.getProjectById(projectId);
       int task = taskService.getTaskById(taskId);
       if (project != null) {
           Task newTask = new Task();
           newTask.setProjectId(projectId);
           newTask.setProject(project);
           newTask.setTaskId(task);
           taskService.createTask(newTask);
           List<Task> tasks = taskService.getTasksByProjectId(projectId);
           model.addAttribute("tasks", tasks);
           model.addAttribute("newTask", new Task());
           model.addAttribute("projectManagers", userService.getProjectManagersByProjectId(projectId));
       }
       model.addAttribute("projectId", projectId); // add project ID to model
       return "create-task";
   }


    @GetMapping("/task/{userId}")
    public String getUserProjects(@PathVariable("userId") int userId, Model model) {
        List<Task> userTasks = taskService.getTasksByProjectId(userId);
        model.addAttribute("projects", userTasks);
        model.addAttribute("user", userService.getUser(userId));
        return "user-frontpage";
    }

    @PostMapping("/create")
    public String createProject(@ModelAttribute Task task, HttpSession httpSession) {
        int user = (int) httpSession.getAttribute("userId");
        task.setUserId(user);
        taskService.createTask(task);
        return "redirect:/smartslate/user/" + user;
    }


    // Update task
    @GetMapping("/tasks/{id}/edit")
    public String editTask(Model model, @PathVariable("id") int taskId) {
        int task = taskService.getTaskById(taskId);
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
        int userId = taskService.getTaskById(taskId);
        taskService.deleteTask(taskId);
        redirectAttributes.addAttribute("userId", userId);
        return "redirect:/user/{userId}";
    }



}
