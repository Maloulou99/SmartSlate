package com.example.smartslate.model;

import java.util.Date;

public class Task {
    private int taskId;
    private String taskName;
    private Date startDate;
    private Date endDate;
    private boolean completed;
    private int projectId;
    private int userId; // Fremmednøgle til User-tabellen
    private int employeeId; // Fremmednøgle til Employee-tabellen

    public Task(int taskId, String taskName, Date startDate, Date endDate, boolean completed, int projectId, int userId, int employeeId) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completed = completed;
        this.projectId = projectId;
        this.userId = userId;
        this.employeeId = employeeId;
    }
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
