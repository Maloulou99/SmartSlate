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

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    public String createTaskForm(@PathVariable int projectId, Model model, @RequestParam(required = false) Integer selectedEmployeeId, HttpSession session) {
        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/login";
        }

        Task task = new Task();
        task.setProjectId(projectId);

        // Hent projektlederens ID baseret på projectId
        int projectManagerId = iProjectRepository.getProjectManagerIdByProjectId(projectId);
        task.setProjectmanagerID(projectManagerId);

        if (selectedEmployeeId != null) {
            User employee = iUserRepository.getUser(selectedEmployeeId);
            if (employee != null) {
                String fullName = employee.getFirstName() + " " + employee.getLastName();
                task.setUserID(employee.getUserID());
            }
        }

        // Hent listen over employees med roleID 3
        List<User> employees = iTaskRepository.getEmployeesWithRoleThree();

        // Fjern projektlederen fra listen over ansatte
        employees = employees.stream().filter(e -> e.getUserID() != projectManagerId).collect(Collectors.toList());


        model.addAttribute("task", task);
        model.addAttribute("employees", employees);
        model.addAttribute("projectId", projectId);
        model.addAttribute("projectManagerId", projectManagerId);

        return "create-task";
    }
    @PostMapping("/projects/{projectId}/createTask")
    public String createTask(@PathVariable int projectId,
                             @ModelAttribute Task task,
                             @RequestParam(value = "selectedEmployee") int selectedEmployeeId,
                             Model model,
                             HttpSession session) {
        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/login";
        }
        int loggedInUserId = userIdObj;

        // Get project details
        Project project = iProjectRepository.getProjectById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project with ID " + projectId + " does not exist.");
        }

        // Set project manager ID
        int projectManagerId = iProjectRepository.getProjectManagerIdByProjectId(projectId);
        task.setProjectmanagerID(projectManagerId);
        task.setUserID(projectManagerId);

        iTaskRepository.getEmployeesByTaskId(task.getTaskId());

        // Set user ID
        task.setUserId(selectedEmployeeId);

        // Create task in the repository
        int taskId = iTaskRepository.createTask(selectedEmployeeId, task.getTaskName(), task.getDescription(), task.getHours(),
                projectId, projectManagerId, task.getStatus());

        // Hvis selectedEmployeeId ikke er 0, associer medarbejderen med opgaven
        if (selectedEmployeeId != 0) {
            List<User> employees = new ArrayList<>();
            User selectedEmployee = iUserRepository.getUser(selectedEmployeeId);
            if (selectedEmployee != null) {
                employees.add(selectedEmployee);
            }
            task.setEmployees(employees);

            // Tilføj denne linje for at associer medarbejderen med opgaven
            iTaskRepository.associateEmployeesWithTask(taskId, Collections.singletonList(selectedEmployeeId));
        }


        // Get the newly created task
        Task createdTask = iTaskRepository.getTaskById(taskId);

        // Get the selected employee
        User selectedEmployee = iUserRepository.getUser(selectedEmployeeId);
        String selectedEmployeeName = selectedEmployee != null ? selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName() : "";

        // Get a list of tasks for the project
        List<Task> tasks = iTaskRepository.getTasksByProjectId(projectId);

        // Get a list of employees
        List<User> employees = iTaskRepository.getEmployeesWithRoleThreeByUserId(selectedEmployeeId);
        task.setEmployees(employees);

        // Add created task to model
        model.addAttribute("task", createdTask);
        model.addAttribute("selectedEmployeeId", selectedEmployeeId);
        model.addAttribute("selectedUserIds", selectedEmployeeId != 0 ? Collections.singletonList(selectedEmployeeId) : null);
        model.addAttribute("selectedEmployeeName", selectedEmployeeName);
        model.addAttribute("tasks", tasks); // Opdateret liste over opgaver
        model.addAttribute("employees", employees);
        model.addAttribute("loggedInUserId", loggedInUserId);
        return "show-tasks";
    }


    @GetMapping("/tasks/{taskId}/update")
    public String showUpdateTaskForm(@PathVariable("taskId") int taskId, Model model) {
        Task task = iTaskRepository.getTaskById(taskId);
        model.addAttribute("task", task);

        // Set project manager ID
        int projectManagerId = iUserRepository.getProjectManagerByTaskId(taskId);
        task.setProjectmanagerID(projectManagerId);

        // Get the current datetime value and set it in the model
        BigDecimal currentDateTime = task.getHours();
        model.addAttribute("currentDateTime", currentDateTime);

        List<User> employees = iTaskRepository.getEmployeesWithRoleThree();
        model.addAttribute("employees", employees);
        model.addAttribute("projectManagerId", projectManagerId);

        return "update-task";
    }

    @PostMapping("/tasks/{taskId}/update/{projectId}")
    public String updateTask(@ModelAttribute("task") Task updatedTask, @PathVariable("taskId") int taskId, @PathVariable("projectId") int projectId) {

        Task existingTask = iTaskRepository.getTaskById(taskId);

        // Set the updated values
        int projectManagerId = iUserRepository.getProjectManagerByTaskId(taskId);
        updatedTask.setProjectmanagerID(projectManagerId);
        updatedTask.setTaskId(taskId);
        existingTask.setTaskName(updatedTask.getTaskName());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setHours(updatedTask.getHours());
        List<User> updatedEmployees = updatedTask.getEmployees();
        existingTask.getEmployees().addAll(updatedEmployees);

        existingTask.setStatus(updatedTask.getStatus());

        iTaskRepository.updateTask(existingTask);

        // Redirect to task details page
        return "redirect:/projects/" + projectId + "/tasks";
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


    @GetMapping("/projects/{projectId}/tasks")
    public String getTasksByProjectId(@PathVariable int projectId, Model model, HttpSession session) {
        Integer loggedInUserIdObj = (Integer) session.getAttribute("userId");
        if (loggedInUserIdObj == null) {
            return "redirect:/login";
        }
        int loggedInUserId = loggedInUserIdObj;

        List<Task> tasks = iTaskRepository.getTasksByProjectId(projectId);
        int projectManagerId = iProjectRepository.getProjectManagerIdByProjectId(projectId);

        // Set project manager ID for each task
        for (Task task : tasks) {
            task.setProjectmanagerID(projectManagerId);
            List<User> employeIds = iTaskRepository.getEmployeesByTaskId(task.getTaskId());
            int idByTaskId = iTaskRepository.getUserIdByTaskId(task.getTaskId()); // Hent brugerens ID baseret på opgavens ID
            task.setUserId(idByTaskId);
            task.setUserId(employeIds);
            iTaskRepository.getTaskByProjectId(projectId);
            BigDecimal hours = task.getHours();  // Assuming the getter method for hours is getHours()
            LocalTime localTime = LocalTime.of(hours.intValue(), 0);
            String formattedTime = localTime.format(DateTimeFormatter.ofPattern("HH:mm"));

            model.addAttribute("formattedTime", formattedTime);
            model.addAttribute("idByTaskId", idByTaskId);
        }
        Project project = iProjectRepository.getProjectById(projectId);

        model.addAttribute("tasks", tasks);
        model.addAttribute("projectName", project.getProjectName());
        model.addAttribute("projectId", projectId);
        model.addAttribute("projectManager", projectManagerId);
        model.addAttribute("loggedInUserId", loggedInUserId);
        return "show-tasks";
    }

    @GetMapping("/show/task/employees/{projectId}")
    public String showTaskInformation(@PathVariable int projectId, Model model, HttpSession session) {
        Integer loggedInUserIdObj = (Integer) session.getAttribute("userId");
        if (loggedInUserIdObj == null) {
            return "redirect:/login";
        }

        int taskId = iTaskRepository.getTaskIdByProjectId(projectId);
        List<Task> tasks = iTaskRepository.getTasksByProjectId(projectId);
        Task task = iTaskRepository.getTaskById(taskId);

        model.addAttribute("task", task);
        model.addAttribute("tasks", tasks);
        model.addAttribute("loggedInUserIdObj", loggedInUserIdObj);
        model.addAttribute("ListOfUserLists", iTaskRepository.getListOfUserLists(tasks));
        model.addAttribute("projectId", projectId);
        return "show-task-information";
    }




    //Sender brugeren tilbage på user-frontpage med alle information om projekter
    @GetMapping("/projects/tasks/{projectId}/{userId}")
    public String showTasksForProject(@PathVariable int projectId, @PathVariable int userId, Model model, HttpSession session) {
        Integer loggedInUserIdObj = (Integer) session.getAttribute("userId");
        if (loggedInUserIdObj == null) {
            return "redirect:/login";
        }
        //int loggedInUserId = loggedInUserIdObj;

        // Hent tasks for det angivne projectId fra databasen
        List<Task> tasks = iTaskRepository.getTasksByProjectManagerID(projectId);

        // Hent medarbejdere for det angivne projectId fra databasen
        List<User> employees = iUserRepository.getEmployeesByProjectId(projectId);
        User projectManagers = iUserRepository.getProjectManagerByProjectId(projectId);

        // Tilføj tasks, projectId og employees til modellen
        model.addAttribute("tasks", tasks);
        model.addAttribute("projectId", projectId);
        model.addAttribute("employees", employees);
        model.addAttribute("projectManagers", projectManagers);

        // Omdirigér til brugerens forside med bruger-id som en path-variabel
        return "redirect:/smartslate/user/" + userId;
    }
}


