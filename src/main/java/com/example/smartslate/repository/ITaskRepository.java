package com.example.smartslate.repository;

import com.example.smartslate.model.Task;

import java.util.List;

public interface ITaskRepository {

    int createTask(Task task, int projectId, int projectManagerId);
    List<Task> getTasksByProjectId(int projectId);
    void updateTask(Task task);
    void deleteTask(int taskId);
    List<Task> getAllTasks();
    List<Task> getTasksByProjectManagerID(int projectManagerID);
    Task getTaskById(int taskId);
}
