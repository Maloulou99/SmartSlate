package com.example.smartslate.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Task {
    private int taskId;
    private int projectId;
    private String taskName;
    private String description;
    private String deadline;
    private Integer projectmanagerID;
    private Integer userID;
    private String status;
    private Project project;
    private int userId; // Fremmednøgle til User-tabellen
    private List<User> employees = new ArrayList<>();


    public Task(int taskId, int projectId, String taskName, String description, String deadline, Integer projectmanagerID, Integer userID, String status, Project project, int userId) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.taskName = taskName;
        this.description = description;
        this.deadline = deadline;
        this.projectmanagerID = projectmanagerID;
        this.userID = userID;
        this.status = status;
        this.project = project;
        this.userId = userId;
    }
    public Task(List<User> employees){
        this.employees = employees;
    }

    public Task() {

    }

    public Task(int taskId, String taskName, String description, String deadline, String status, int projectId, Integer projectManagerId, Integer userId) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.taskName = taskName;
        this.description = description;
        this.deadline = deadline;
        this.projectmanagerID = projectManagerId;
        this.userID = userId;
        this.status = status;
        this.userId = userId;

    }




    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Task(String taskName, String description, String deadline, int projectmanagerID, String status) {
        this.taskName = taskName;
        this.description = description;
        this.deadline = deadline;
        this.projectmanagerID = projectmanagerID;
        this.status = status;
    }

    public List<User> getEmployees() {
        return employees;
    }

    public void setEmployees(List<User> employees) {
        this.employees = employees;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public Integer getProjectmanagerID() {
        return projectmanagerID;
    }

    public void setProjectmanagerID(Integer projectmanagerID) {
        this.projectmanagerID = projectmanagerID;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}