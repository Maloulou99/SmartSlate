package com.example.smartslate.repository;

import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface ITaskRepository {

    public int createTask(int userID, String taskName, String description, BigDecimal hours, int projectID, int projectManagerID, String status);
    List<Task> getTasksByProjectId(int projectId);
    void updateTask(Task task);
    List<Task> getAllTasks();
    List<Task> getTasksByProjectManagerID(int projectManagerID);
    Task getTaskById(int taskId);
    Task getTaskByProjectId(int projectId);
    void deleteTaskFromProject(int projectId, int taskId);
    void associateEmployeesWithTask(int taskId, List<Integer> employeeIds);
    List<User> getEmployeesWithRoleThreeByUserId(int userId);
    public List<Task> getTasksByEmployeeId(int employeeId);
    List<User> getEmployeesWithRoleThree();
    int getProjectIdByTaskId(int taskId);
    public User getUser(int userId);
    List<User> getEmployeesByTaskId(int taskId);

}
