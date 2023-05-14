package com.example.smartslate.repository;

import com.example.smartslate.model.Task;

import java.util.List;

public interface ITaskRepository {

    int createTask(Task task);
    List<Task> getTasksByProjectId(int projectId);
    void updateTask(Task task);
    void deleteTask(int taskId);
    List<Task> getAllTasks(int userID);
    List<Task> getTasksByProjectManagerID(int projectManagerID);
    Task getTaskById(int taskId);

    void deleteTasksByProjectId(int projectId);

}
