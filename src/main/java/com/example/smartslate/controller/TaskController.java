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

    /*@GetMapping("/projects/{projectId}/createTask")
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
    }*/

    @GetMapping("/projects/{projectId}/createTask")
    public String createTaskForm(@PathVariable int projectId, Model model, HttpSession session) {
        Integer userIdObj = (Integer) session.getAttribute("userId"); // Hent brugerens ID fra session
        if (userIdObj == null) {
            // Brugeren er ikke logget ind, så redirect til login siden eller vis en fejlmeddelelse
            return "redirect:/login";
        }
        int userId = userIdObj;
        User user = userService.getUser(userId); // Hent brugeren med det pågældende ID
        model.addAttribute("user", user);
        Project project = projectService.getProjectById(projectId); // hent projekt med det givne projekt-id
        if (project != null) {
            model.addAttribute("project", project);
            model.addAttribute("task", new Task()); // tilføj en tom task til modelattributterne
            return "create-task";
        } else {
            // Projektet findes ikke, så vis en fejlmeddelelse eller redirect til en anden side
            return "redirect:/projects";
        }
    }

    @PostMapping("/projects/{projectId}/createTask")
    public String createTask(@PathVariable int projectId,
                             @RequestParam(value = "assignedTo", required = false) Integer assignedTo,
                             @RequestParam("description") String description,
                             @RequestParam("deadline") String deadline,
                             @RequestParam("status") String status, Model model, HttpSession session) {
        Integer userIdObj = (Integer) session.getAttribute("userId"); // Hent brugerens ID fra session
        if (userIdObj == null) {
            // Brugeren er ikke logget ind, så redirect til login siden eller vis en fejlmeddelelse
            return "redirect:/login";
        }
        int userId = userIdObj;
        Project project = projectService.getProjectById(projectId);
        if (project != null) {
            Task newTask = new Task();
            newTask.setDescription(description);
            newTask.setDeadline(deadline);
            newTask.setStatus(status);
            if (assignedTo == null || assignedTo == 0) {
                newTask.setUserId(userId);
            } else {
                newTask.setAssignedTo(assignedTo);
            }
            newTask.setProject(project);
            taskService.createTask(newTask);

            // update the project with the new task
            project.getTasks().add(newTask);
            projectService.updateProject(project);

            // retrieve the updated list of tasks for the project
            List<Task> tasks = taskService.getTasksByProjectId(projectId);

            // add the tasks and a new empty task to the model
            model.addAttribute("tasks", tasks);
            model.addAttribute("newTask", new Task());

            // add other model attributes as needed
            model.addAttribute("projectManagers", userService.getProjectManagersByProjectId(projectId));
        }
        model.addAttribute("project", project);
        model.addAttribute("user", userService.getUser(userId));
        model.addAttribute("projectId", projectId); // add project ID to model
        return "create-task";
    }



    /*@PostMapping("/projects/{projectId}/createTask")
    public String createTask(@PathVariable int projectId,
                             @RequestParam("assignedTo") int assignedTo,
                             @RequestParam("description") String description,
                             @RequestParam("deadline") String deadline,
                             @RequestParam("status") String status, Model model) {
        Project project = projectService.getProjectById(projectId);
        if (project != null) {
            Task newTask = new Task();
            newTask.setDescription(description);
            newTask.setDeadline(deadline);
            newTask.setStatus(status);
            newTask.setAssignedTo(assignedTo);
            newTask.setProject(project);

            taskService.createTask(newTask); // save the new task

            // update the project with the new task
            project.getTasks().add(newTask);
            projectService.updateProject(project);

            // retrieve the updated list of tasks for the project
            List<Task> tasks = taskService.getTasksByProjectId(projectId);

            // add the tasks and a new empty task to the model
            model.addAttribute("tasks", tasks);
            model.addAttribute("newTask", new Task());

            // add other model attributes as needed
            model.addAttribute("projectManagers", userService.getProjectManagersByProjectId(projectId));
        }
        model.addAttribute("projectId", projectId); // add project ID to model
        return "create-task";
    }*/


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
        int userId = taskService.getTaskById(taskId).getUserId();
        taskService.deleteTask(taskId);
        redirectAttributes.addAttribute("userId", userId);
        return "redirect:/user/{userId}";
    }



}
