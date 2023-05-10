package com.example.smartslate.service;

import com.example.smartslate.model.Task;
import com.example.smartslate.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }
    public int createTask(Task task){
        return taskRepository.createTask(task);
    }

    public List<Task> getTasksByProjectId(int projectId){
        return taskRepository.getTasksByProjectId(projectId);
    }
    public List<Task> getAllTasks(){
        return taskRepository.getAllTasks();
    }
    public List<Task> getTasksByAssignedUser(String assignedUser){
        return taskRepository.getTasksByAssignedUser(assignedUser);
    }
    public void updateTask(Task task) {
        taskRepository.updateTask(task);
    }
    public void deleteTask(int taskId) {
        taskRepository.deleteTask(taskId);
    }
    public int getTaskById(int taskId){
        return taskRepository.getTaskById(taskId);
    }

}
