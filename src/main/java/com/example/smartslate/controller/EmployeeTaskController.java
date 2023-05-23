package com.example.smartslate.controller;

import com.example.smartslate.model.Task;
import com.example.smartslate.repository.ILoginRepository;
import com.example.smartslate.repository.IProjectRepository;
import com.example.smartslate.repository.ITaskRepository;
import com.example.smartslate.repository.IUserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequestMapping("/")
@Controller
public class EmployeeTaskController {

    private IProjectRepository iProjectRepository;
    private ITaskRepository iTaskRepository;
    private IUserRepository iUserRepository;
    private ILoginRepository iLoginRepository;

    public EmployeeTaskController(IProjectRepository iProjectRepository, ITaskRepository iTaskRepository, IUserRepository iUserRepository, ILoginRepository iLoginRepository) {
        this.iProjectRepository = iProjectRepository;
        this.iTaskRepository = iTaskRepository;
        this.iUserRepository = iUserRepository;
        this.iLoginRepository = iLoginRepository;
    }

    @GetMapping("/employees/{employeeId}/calculate")
    public String calculateTime(@PathVariable("employeeId") int employeeId, Model model) {
        List<Task> tasks = iTaskRepository.getTasksByEmployeeId(employeeId);
        int totalTimeSpent = calculateTotalTimeSpent(tasks);
        model.addAttribute("totalTimeSpent", totalTimeSpent);
        return "calculate-time";
    }

    private int calculateTotalTimeSpent(List<Task> tasks) {
        int totalTimeSpent = 0;

        for (Task task : tasks) {
            BigDecimal hours = task.getHours();
            int hoursAsInt = hours.intValue(); // Konverter BigDecimal til integer
            totalTimeSpent += hoursAsInt;
        }

        return totalTimeSpent;
    }


    @GetMapping("/calculate/task")
    public String showTasks(@RequestParam("userId") int userId, Model model) {
        List<Task> tasks = iTaskRepository.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "calculate-time";
    }

}
