package com.example.smartslate.controller;

import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;
import com.example.smartslate.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequestMapping("/")
@Controller
public class EmployeeTaskController {

    private IProjectRepository iProjectRepository;
    private ITaskRepository iTaskRepository;
    private IUserRepository iUserRepository;
    private ILoginRepository iLoginRepository;
    private IEmployeeTaskRepository iEmployeeTaskRepository;

    public EmployeeTaskController(IProjectRepository iProjectRepository, ITaskRepository iTaskRepository, IUserRepository iUserRepository, ILoginRepository iLoginRepository, IEmployeeTaskRepository iEmployeeTaskRepository) {
        this.iProjectRepository = iProjectRepository;
        this.iTaskRepository = iTaskRepository;
        this.iUserRepository = iUserRepository;
        this.iLoginRepository = iLoginRepository;
        this.iEmployeeTaskRepository = iEmployeeTaskRepository;
    }


    @GetMapping("/employees/{projectId}/calculate")
    public String calculateTime(@PathVariable("projectId") int projectId, Model model, HttpSession session) {
        Integer loggedInUserIdObj = (Integer) session.getAttribute("userId");
        if (loggedInUserIdObj == null) {
            return "redirect:/login";
        }
        iProjectRepository.getProjectById(projectId);
        List<Task> tasks = iTaskRepository.getTasksByProjectId(projectId);

        for (Task task : tasks) {
            int taskId = task.getTaskId();
            int userId = task.getUserID();
            String taskName = task.getTaskName();
            task.setTaskId(taskId);
            task.setUserID(userId);
            task.setTaskName(taskName);
            BigDecimal hours = task.getHours();
            LocalTime localTime = LocalTime.of(hours.intValue(), 0);
            String formattedTime = localTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            task.setHours(hours);
            model.addAttribute("formattedTime", formattedTime);
            // Hent medarbejderen baseret på userId og tilføj den til taskens medarbejderliste
            User employee = iUserRepository.getEmployeeUser(userId);
            task.getEmployees().add(employee);
            model.addAttribute("employees", iTaskRepository.getListOfUserLists(tasks));
            model.addAttribute("task", task);
        }

        int totalTimeSpent = iEmployeeTaskRepository.calculateTotalTimeSpent(tasks);

        model.addAttribute("tasks", tasks);
        model.addAttribute("totalTimeSpent", totalTimeSpent);
        model.addAttribute("loggedInUserIdObj", loggedInUserIdObj);

        return "calculate-time";
    }



    @GetMapping("/calculate/task")
    public String showTasks(@RequestParam("userId") int userId, Model model) {
        List<Task> tasks = iTaskRepository.getAllTasks(userId);
        int totalTimeSpent = iEmployeeTaskRepository.calculateTotalTimeSpent(tasks);
        model.addAttribute("tasks", tasks);
        model.addAttribute("totalTimeSpent", totalTimeSpent);
        model.addAttribute("listOfUsers", iTaskRepository.getListOfUserLists(tasks));
        return "calculate-time";
    }


}
