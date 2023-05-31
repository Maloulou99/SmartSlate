package com.example.smartslate.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Task {
    private int taskId;
    private int projectId;
    private String taskName;
    private String description;
    private BigDecimal hours;
    private Integer projectmanagerID;
    private Integer userID;
    private String status;
    private Project project;
    private String projectName;
    private List<User> employees = new ArrayList<>();

    private User user;
    public Task(int taskId, int projectId, String taskName, String description, BigDecimal hours, Integer projectmanagerID, Integer userID, String status, Project project) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.taskName = taskName;
        this.description = description;
        this.hours = hours;
        this.projectmanagerID = projectmanagerID;
        this.userID = userID;
        this.status = status;
        this.project = project;
    }
    public Task(List<User> employees){
        this.employees = employees;
    }

    public Task() {

    }

    public Task(int taskId, String taskName, String description, BigDecimal hours, String status, int projectId, Integer projectManagerId) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.taskName = taskName;
        this.description = description;
        this.hours = hours;
        this.projectmanagerID = projectManagerId;
        this.status = status;
    }

    public Task(int taskID, String taskName, String description, BigDecimal hours, String status, String projectName) {
        this.taskId = taskID;
        this.taskName = taskName;
        this.description = description;
        this.hours = hours;
        this.status = status;
        this.projectName = projectName;
    }

    public Task(int taskId, String taskName, String description, BigDecimal hours, String status, int projectId, int projectManagerId, int userId) {
        this.taskName = taskName;
        this.description = description;
        this.hours = hours;
        this.status = status;
        this.projectId = projectId;
        this.projectmanagerID = projectManagerId;
        this.userID = userId;
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Task(String taskName, String description, BigDecimal hours, int projectmanagerID, String status) {
        this.taskName = taskName;
        this.description = description;
        this.hours = hours;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public BigDecimal getHours() {
        return hours;
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



    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }


}