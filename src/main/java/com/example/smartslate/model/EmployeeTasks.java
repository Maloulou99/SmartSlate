package com.example.smartslate.model;

public class EmployeeTasks {

    private int employeeTaskID;
    private int taskEmployeeID;
    private int taskID;
    private String hours;

    public EmployeeTasks(int employeeTaskID, int taskEmployeeID, int taskID, String hours) {
        this.employeeTaskID = employeeTaskID;
        this.taskEmployeeID = taskEmployeeID;
        this.taskID = taskID;
        this.hours = hours;
    }

    public int getEmployeeTaskID() {
        return employeeTaskID;
    }

    public void setEmployeeTaskID(int employeeTaskID) {
        this.employeeTaskID = employeeTaskID;
    }

    public int getTaskEmployeeID() {
        return taskEmployeeID;
    }

    public void setTaskEmployeeID(int taskEmployeeID) {
        this.taskEmployeeID = taskEmployeeID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}
