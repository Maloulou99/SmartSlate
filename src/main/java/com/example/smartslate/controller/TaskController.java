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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        List<User> employees = iUserRepository.getEmployeesByProjectId(projectId);
        Task task = new Task();
        task.setProjectId(projectId);

        // Hent projektlederens ID baseret på projectId
        int projectManagerId = iProjectRepository.getProjectManagerIdByProjectId(projectId);
        task.setProjectmanagerID(projectManagerId);

        // Fjern projektlederen fra listen over ansatte
        employees = employees.stream().filter(e -> e.getUserID() != projectManagerId).collect(Collectors.toList());

        model.addAttribute("task", task);
        model.addAttribute("employees", employees);
        model.addAttribute("projectId", projectId);

        return "create-task";
    }


   /* @PostMapping("/projects/{projectId}/createTask")
    public String createTask(@PathVariable int projectId,
                             @ModelAttribute Task task,
                             @RequestParam int projectManagerId,
                             @RequestParam(value = "selectedEmployees", required = false) List<Integer> selectedEmployees,
                             Model model,
                             HttpSession session) {
        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/login";
        }
        int userId = userIdObj;

        // Set project manager ID on task object
        task.setProjectmanagerID(projectManagerId);

        // Create task in the repository
        int taskId = iTaskRepository.createTask(userId, task.getTaskName(), task.getDescription(), task.getDeadline(),
                projectId, task.getProjectmanagerID(), task.getStatus());

        // If selectedEmployees is not null or empty, associate employees with the task
        if (selectedEmployees != null && !selectedEmployees.isEmpty()) {
            iTaskRepository.associateEmployeesWithTask(taskId, selectedEmployees);
        }

        // Get the newly created task
        Task createdTask = iTaskRepository.getTaskById(taskId);

        // Add common model attributes
        getCommonModelAttributes(model, projectId, userId);
        // Add created task to model
        model.addAttribute("task", createdTask);
        model.addAttribute("selectedUserIds", selectedEmployees);
        return "show-tasks";
    }*/



    @PostMapping("/projects/{projectId}/createTask")
    public String createTask(@PathVariable int projectId,
                             @ModelAttribute Task task,
                             @RequestParam int projectManagerId,
                             @RequestParam(value = "selectedEmployees", required = false) List<Integer> selectedEmployees,
                             Model model,
                             HttpSession session) {
        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/login";
        }
        int userId = userIdObj;

        // Set project manager ID and user ID on task object
        task.setProjectmanagerID(projectManagerId);

        // Create task in the repository
        int taskId = iTaskRepository.createTask(userId, task.getTaskName(), task.getDescription(), task.getDeadline(),
                projectId, task.getProjectmanagerID(), task.getStatus());

        // If selectedEmployees is not null or empty, associate employees with the task
        int selectedUserId = 0;
        if (selectedEmployees != null && !selectedEmployees.isEmpty()) {
            selectedUserId = selectedEmployees.get(0);
            iTaskRepository.associateEmployeesWithTask(taskId, selectedEmployees);
            task.setUserID(selectedUserId);
        }

        // Get the newly created task
        Task createdTask = iTaskRepository.getTaskById(taskId);
        model.addAttribute("selectedUserId", selectedUserId);
        // Add common model attributes
        getCommonModelAttributes(model, projectId, userId);
        // Add created task to model
        model.addAttribute("task", createdTask);
        model.addAttribute("selectedUserIds", selectedEmployees);
        model.addAttribute("projectId", projectId);
        model.addAttribute("userId", userId);
        return "show-tasks";
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

    private void getCommonModelAttributes(Model model, int projectId, int userId) {
        List<Task> tasks = iTaskRepository.getTasksByProjectId(projectId);
        Project project = iProjectRepository.getProjectById(projectId);
        Map<Integer, List<String>> employeeNamesByTask = new HashMap<>(); // Ny map til medarbejdernavne efter taskID

        for (Task task : tasks) {

            List<User> employees = iTaskRepository.getEmployeesWithRoleThreeByUserId(task.getTaskId());
            List<String> employeeNames = new ArrayList<>();
            for (User employee : employees) {
                employeeNames.add(employee.getFirstName() + " " + employee.getLastName());
            }
            employeeNamesByTask.put(task.getTaskId(), employeeNames);

            model.addAttribute("task", task); // Tilføj opgaveobjekt til model med en unik nøgle baseret på taskID
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("projectName", project.getProjectName());
        model.addAttribute("projectId", projectId);
        model.addAttribute("userId", userId);
        model.addAttribute("employeeNamesByTask", employeeNamesByTask); // Tilføj employeeNamesByTask til model

    }

    @GetMapping("/projects/{projectId}/tasks")
    public String getTasksByProjectId(@PathVariable int projectId, Model model, HttpSession session) {
        Integer loggedInUserIdObj = (Integer) session.getAttribute("userId");
        if (loggedInUserIdObj == null) {
            return "redirect:/login";
        }
        int loggedInUserId = loggedInUserIdObj;

       getCommonModelAttributes(model, projectId, loggedInUserId);

        return "show-tasks";
    }

    //Sender brugeren tilbage på user-frontpage med alle information om projekter
    @GetMapping("/projects/tasks/{projectId}/{userId}")
    public String showTasksForProject(@PathVariable int projectId, @PathVariable int userId, Model model, HttpSession session) {
        Integer loggedInUserIdObj = (Integer) session.getAttribute("userId");
        if (loggedInUserIdObj == null) {
            return "redirect:/login";
        }
        int loggedInUserId = loggedInUserIdObj;

        // Hent tasks for det angivne projectId fra databasen
        List<Task> tasks = iTaskRepository.getTasksByProjectManagerID(projectId);

        // Hent medarbejdere for det angivne projectId fra databasen
        List<User> employees = iUserRepository.getEmployeesByProjectId(projectId);

        // Tilføj tasks, projectId og employees til modellen
        model.addAttribute("tasks", tasks);
        model.addAttribute("projectId", projectId);
        model.addAttribute("employees", employees);

        // Omdirigér til brugerens forside med bruger-id som en path-variabel
        return "redirect:/smartslate/user/" + userId;
    }
}



