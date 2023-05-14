package com.example.smartslate.controller;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;
import com.example.smartslate.repository.IProjectRepository;
import com.example.smartslate.repository.ITaskRepository;
import com.example.smartslate.repository.IUserRepository;
import jakarta.servlet.http.HttpServletRequest;
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

        // Get the newly created task
        Task createdTask = iTaskRepository.getTaskById(taskId);

        // Add common model attributes
        getCommonModelAttributes(model, projectId, userId);

        // Add created task to model
        model.addAttribute("task", createdTask);
        model.addAttribute("task", task);

        return "created-task";
    }
    // Update task
    @GetMapping("/tasks/{taskId}/update")
    public String showUpdateTaskForm(@PathVariable("taskId") int taskId, Model model) {
        Task task = iTaskRepository.getTaskById(taskId);
        model.addAttribute("task", task);

        User projectManager = new User();
        model.addAttribute("projectManager", projectManager);

        List<User> projectManagers = iUserRepository.getProjectManagersByRoleId();
        model.addAttribute("projectManagers", projectManagers);

        return "update-task";
    }

    @PostMapping("/tasks/{taskId}/update")
    public String updateTask(@ModelAttribute("task") Task task, @ModelAttribute("user") User user, @PathVariable("taskId") int taskId) {
        // Set task ID
        task.setTaskId(taskId);

        // Get all users with role ID 2
        List<User> projectManagers = iUserRepository.getProjectManagersByRoleId();

        // Set projectManagerID to first project manager found and get full name
        if (!projectManagers.isEmpty()) {
            User projectManager = projectManagers.get(0);
            task.setProjectmanagerID(projectManager.getUserID());
            User projectManagerUser = iUserRepository.getUser(projectManager.getUserID());
            String projectManagerFirstName = projectManagerUser.getFirstName();
            String projectManagerLastName = projectManagerUser.getLastName();
            user.setFirstName(projectManagerFirstName);
            user.setLastName(projectManagerLastName);
        }

        iTaskRepository.updateTask(task);

        // Redirect to task details page
        return "redirect:/tasks/" + taskId;
    }


    // Delete task
    @PostMapping("/projects/{projectId}/tasks/{taskId}/delete")
    public String deleteTask(@PathVariable int projectId,
                             @PathVariable int taskId,
                             HttpSession session,
                             HttpServletRequest request) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        iTaskRepository.deleteTaskFromProject(projectId, taskId);

        String referer = request.getHeader("Referer");
        if (referer == null || !referer.contains("/projects/" + projectId + "/tasks")) {
            return "redirect:/projects/" + projectId + "/tasks";
        }
        return "redirect:" + referer;
    }

    private Model getCommonModelAttributes(Model model, int projectId, int userId) {
        List<Task> tasks = iTaskRepository.getTasksByProjectId(projectId);
        Project project = iProjectRepository.getProjectById(projectId);
        User projectCreator = iUserRepository.getUser(iUserRepository.getUserIdByProjectId(projectId));

        List<String> userFirstNames = new ArrayList<>();
        List<String> userLastNames = new ArrayList<>();
        List<String> pmFirstNames = new ArrayList<>();
        List<String> pmLastNames = new ArrayList<>();

        for (Task task : tasks) {
            User user = iUserRepository.getUser(task.getUserId());
            userFirstNames.add(user.getFirstName());
            userLastNames.add(user.getLastName());
            User pm = iUserRepository.getUser(task.getProjectmanagerID());
            pmFirstNames.add(pm.getFirstName());
            pmLastNames.add(pm.getLastName());

            model.addAttribute("task", task);  // Add task object to model with a unique key based on task ID
        }

        pmFirstNames.add(projectCreator.getFirstName());
        pmLastNames.add(projectCreator.getLastName());

        model.addAttribute("tasks", tasks);
        model.addAttribute("projectName", project.getProjectName());
        model.addAttribute("projectId", projectId);
        model.addAttribute("userId", userId);
        model.addAttribute("projectCreatorFirstName", projectCreator.getFirstName());
        model.addAttribute("projectCreatorLastName", projectCreator.getLastName());
        model.addAttribute("userFirstNames", userFirstNames);
        model.addAttribute("userLastNames", userLastNames);
        model.addAttribute("pmFirstNames", pmFirstNames);
        model.addAttribute("pmLastNames", pmLastNames);

        return model;
    }


    @GetMapping("/projects/{projectId}/tasks")
    public String getTasksByProjectId(@PathVariable int projectId, Model model, HttpSession session) {
        Integer loggedInUserIdObj = (Integer) session.getAttribute("userId");
        if (loggedInUserIdObj == null) {
            return "redirect:/login";
        }
        int loggedInUserId = loggedInUserIdObj;

        model = getCommonModelAttributes(model, projectId, loggedInUserId);

        return "created-task";
    }

    //Sender brugeren tilbage på user-frontpage med alle information om projekter
    @GetMapping("/projects/tasks/{projectId}/{userId}")
    public String showTasksForProject(@PathVariable int projectId, @PathVariable int userId, Model model, HttpSession session) {
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
        //model.addAttribute("userId", loggedInUserId);

        // redirect to the user-frontpage with the user id as a path variable
        return "redirect:/smartslate/user/" + userId;
    }


}



