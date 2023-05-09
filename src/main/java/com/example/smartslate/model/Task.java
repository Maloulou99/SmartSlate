package com.example.smartslate.model;

import java.util.Date;
import java.util.List;

public class Task {
    private int taskId;
    private int projectId;
    private String description;
    private String deadline;
    private String assignedTo;
    private String status;
    private Project project;

    private int userId; // Fremmednøgle til User-tabellen

    public Task(int taskId, int projectId, String description, String deadline, String assignedTo, String status, Project project, int userId) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.description = description;
        this.deadline = deadline;
        this.assignedTo = assignedTo;
        this.status = status;
        this.project = project;
        this.userId = userId;
    }

    public Task(){

    }

    public Task(String description, String deadline, String status, Project project) {
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

}