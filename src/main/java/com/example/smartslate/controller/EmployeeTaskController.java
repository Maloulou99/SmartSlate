package com.example.smartslate.controller;

import com.example.smartslate.model.Task;
import com.example.smartslate.repository.ILoginRepository;
import com.example.smartslate.repository.IProjectRepository;
import com.example.smartslate.repository.ITaskRepository;
import com.example.smartslate.repository.IUserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
        List<Task> tasks = iTaskRepository.getTasksByProjectId(employeeId);
        int totalTimeSpent = calculateTotalTimeSpent(tasks);
        model.addAttribute("totalTimeSpent", totalTimeSpent);
        return "redirect:/tasks/";
    }
    private int calculateTotalTimeSpent(List<Task> tasks) {
        int totalTimeSpent = 0;
        for (Task task : tasks) {
            totalTimeSpent += task.getTimeSpent();
        }
        return totalTimeSpent;
    }


    @GetMapping("/tasks")
    public String showTasks(int userId, Model model) {
        List<Task> tasks = iTaskRepository.getAllTasks(userId);
        model.addAttribute("tasks", tasks);
        return "task-list";
    }

    @PostMapping("/tasks/{taskId}/updateTime")
    public String updateTime(@PathVariable("taskId") int taskId, @RequestParam("employeeId") int employeeId, @RequestParam("updatedTime") LocalTime updatedTime) {

        Task task = iTaskRepository.getTaskById(taskId);
        LocalTime currentTime = task.getTimeSpent();

        LocalTime newTime = currentTime.plusHours(updatedTime.getHour())
                .plusMinutes(updatedTime.getMinute())
                .plusSeconds(updatedTime.getSecond());

        task.setTimeSpent(newTime);
        iTaskRepository.updateTask(task);

        return "redirect:/tasks/" + taskId;
    }
}
