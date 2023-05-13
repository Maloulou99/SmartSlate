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

        List<User> projectManagers = iUserRepository.getProjectManagersByRoleId();
        Project project = iProjectRepository.getProjectById(projectId);
        List<Task> tasks = iTaskRepository.getTasksByProjectId(projectId);
        int projectCreatorId = iUserRepository.getUserIdByProjectId(projectId); // henter brugerid'et for den bruger som har oprettet projektet
        User projectCreator = iUserRepository.getUser(projectCreatorId);

        model.addAttribute("project", project);
        model.addAttribute("task", new Task());
        model.addAttribute("projectManagers", projectManagers);
        model.addAttribute("projectId", projectId);
        model.addAttribute("tasks", tasks);
        model.addAttribute("projectCreatorFirstName", projectCreator.getFirstName());
        model.addAttribute("projectCreatorLastName", projectCreator.getLastName());

        return "create-task";
    }

    @PostMapping("/projects/{projectId}/createTask")
    public String createTask(@PathVariable int projectId,
                             @ModelAttribute Task task,
                             @RequestParam int projectManagerId,
                             Model model,
                             HttpSession session) {
        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/login";
        }
        int userId = userIdObj;

        // Set project manager ID and user ID on task object
        task.setProjectmanagerID(projectManagerId);
        task.setUserId(userId);

        // Create task and get its ID
        int taskId = iTaskRepository.createTask(task);

        // Get User object based on projectManagerId
        User projectManager = iUserRepository.getProjectManagerById(projectManagerId);

        // Update task's project ID with the correct value
        Task createdTask = iTaskRepository.getTaskById(taskId);
        createdTask.setProjectId(projectId);

        // Get user object for the project's creator
        int projectCreatorId = iUserRepository.getUserIdByProjectId(projectId);
        User projectCreator = iUserRepository.getUser(projectCreatorId);

        // Add attributes to the model
        model.addAttribute("task", createdTask);
        model.addAttribute("projectManagerFirstName", projectManager.getFirstName());
        model.addAttribute("projectManagerLastName", projectManager.getLastName());
        model.addAttribute("projectCreatorFirstName", projectCreator.getFirstName());
        model.addAttribute("projectCreatorLastName", projectCreator.getLastName());
        model.addAttribute("projectCreatorId", projectCreatorId);

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

        if (task.getProjectmanagerID() != userId) {
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
        Integer loggedInUserIdObj = (Integer) session.getAttribute("userId");
        if (loggedInUserIdObj == null) {
            return "redirect:/login";
        }
        int loggedInUserId = loggedInUserIdObj;

        List<Task> tasks = iTaskRepository.getTasksByProjectId(projectId);
        Project project = iProjectRepository.getProjectById(projectId);

        int projectManagerId = iUserRepository.getUserIdByProjectId(projectId);
        User projectManager = null;
        String projectManagerFirstName = null;
        String projectManagerLastName = null;
        if (projectManagerId != 0) {
            projectManager = iUserRepository.getProjectManagersFullNames(projectManagerId);
            projectManagerFirstName = projectManager.getFirstName();
            projectManagerLastName = projectManager.getLastName();
        }

        int userId = iUserRepository.getUserIdByProjectId(projectId);
        User user = null;
        String userFirstNames = null;
        String userLastNames = null;

        if (userId != 0) {
            user = iUserRepository.getUserFullNames(userId);
            userFirstNames = user.getFirstName();
            userLastNames = user.getLastName();
        }

        model.addAttribute("projectManagerFirstName", projectManagerFirstName);
        model.addAttribute("projectManagerLastName", projectManagerLastName);
        model.addAttribute("tasks", tasks);
        model.addAttribute("projectName", project.getProjectName());
        model.addAttribute("projectId", projectId);
        model.addAttribute("userFirstNames", userFirstNames);
        model.addAttribute("userLastNames", userLastNames);
        model.addAttribute("userId", loggedInUserId);


        return "created-task";
    }





    //Sender brugeren tilbage på user-frontpage med alle information om projekter
    @GetMapping("/projects/tasks/{projectId}/{userId}")
    public String showTasksForProject(@PathVariable int projectId,@PathVariable int userId, Model model, HttpSession session) {
        Integer loggedInUserIdObj = (Integer) session.getAttribute("userId");
        if (loggedInUserIdObj == null) {
            return "redirect:/login";
        }
        int loggedInUserId = loggedInUserIdObj;

        // code to fetch tasks for the given projectId from the database
        List<Task> tasks = iTaskRepository.getTasksByProjectManagerID(projectId);

        // add tasks and projectId to the model
        model.addAttribute("tasks", tasks);
        model.addAttribute("projectId", projectId);
        model.addAttribute("userId", loggedInUserId);

        // redirect to the user-frontpage with the user id as a path variable
        return "redirect:/smartslate/user/" + loggedInUserId;
    }






}



