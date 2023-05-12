package com.example.smartslate.model;

import java.time.LocalDate;

import java.util.List;


public class Project {
    private int projectId;
    private int userID;
    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String budget;
    private String status;
    private List<Task> tasks;

    public Project(int projectId, int userID, String projectName, String description, LocalDate startDate, LocalDate endDate, String budget, String status, List<Task> tasks) {
        this.projectId = projectId;
        this.userID = userID;
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.status = status;
        this.tasks = tasks;
    }

   public Project(){

   }
    public int getProjectId() {
        return projectId;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

