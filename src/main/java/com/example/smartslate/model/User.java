package com.example.smartslate.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class User {
    private int userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private ArrayList<Project> projects;
    private ArrayList<Task> tasks;


    public User(int userId, String userName, String firstName, String lastName, String email, String password, String phoneNumber, String role, LocalDate createdAt, LocalDate updatedAt, ArrayList<Project> projects, ArrayList<Task> tasks) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.projects = projects;
        this.tasks = tasks;
    }

    public User(int userId, String userName, String firstName, String lastName, String email, String password, String phoneNumber, String role, LocalDate createdAt, LocalDate updatedAt, ArrayList<Project> projects) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.projects = projects;
    }

    public User() {

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}

