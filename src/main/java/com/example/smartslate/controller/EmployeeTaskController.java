package com.example.smartslate.controller;

import com.example.smartslate.model.Task;
import com.example.smartslate.repository.*;
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
    private IEmployeeTask iEmployeeTask;

    public EmployeeTaskController(IProjectRepository iProjectRepository, ITaskRepository iTaskRepository, IUserRepository iUserRepository, ILoginRepository iLoginRepository, IEmployeeTask iEmployeeTask) {
        this.iProjectRepository = iProjectRepository;
        this.iTaskRepository = iTaskRepository;
        this.iUserRepository = iUserRepository;
        this.iLoginRepository = iLoginRepository;
        this.iEmployeeTask = iEmployeeTask;
    }


    @GetMapping("/employees/{employeeId}/calculate")
    public String calculateTime(@PathVariable("employeeId") int employeeId, Model model) {
        List<Task> tasks = iTaskRepository.getTasksByEmployeeId(employeeId);
        for (Task task : tasks) {
            int taskId = task.getTaskId();
            int userId = task.getUserId();
            String taskName = task.getTaskName();
            BigDecimal hours = task.getHours();
            task.setTaskId(taskId);
            task.setUserID(userId);
            task.setTaskName(taskName);
            task.setHours(hours);
        }
        String totalTimeSpent = iEmployeeTask.calculateTotalTimeSpent(tasks);
        model.addAttribute("tasks", tasks);
        model.addAttribute("totalTimeSpent", totalTimeSpent);
        return "calculate-time";
    }



    @GetMapping("/calculate/task")
    public String showTasks(@RequestParam("userId") int userId, Model model) {
        List<Task> tasks = iTaskRepository.getAllTasks(userId);
        String totalTimeSpent = iEmployeeTask.calculateTotalTimeSpent(tasks);
        model.addAttribute("tasks", tasks);
        model.addAttribute("totalTimeSpent", totalTimeSpent);
        return "calculate-time";
    }


}
