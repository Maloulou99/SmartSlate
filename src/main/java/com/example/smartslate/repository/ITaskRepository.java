package com.example.smartslate.repository;

import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;

import java.util.List;

public interface ITaskRepository {

    int createTask(int userID, String taskName, String description, String deadline, int projectID, int projectManagerID, String status);
    List<Task> getTasksByProjectId(int projectId);
    void updateTask(Task task);
    List<Task> getAllTasks(int userID);
    List<Task> getTasksByProjectManagerID(int projectManagerID);
    Task getTaskById(int taskId);

    void deleteTaskFromProject(int projectId, int taskId);
    void associateEmployeesWithTask(int taskId, List<Integer> employeeIds);
    List<User> getEmployeesWithRoleThreeByUserId(int userId);
    void associateProjectManagerWithTask(int taskId, Integer projectManagerId);
    public List<Task> getTasksByEmployeeId(int employeeId);
    List<User> getEmployeesWithRoleThree();
    int getProjectIdByTaskId(int taskId);

}
