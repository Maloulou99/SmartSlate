package com.example.smartslate.repository;

import com.example.smartslate.model.Task;

import java.util.List;

public interface ITaskRepository {

    int createTask(String name, String desc, String deadline, int projectId, int projectManagerId, String status);
    List<Task> getTasksByProjectId(int projectId);
    void updateTask(Task task);
    void deleteTask(int taskId);
    List<Task> getAllTasks();
    List<Task> getTasksByProjectManagerID(int projectManagerID);
    Task getTaskById(int taskId);


}
