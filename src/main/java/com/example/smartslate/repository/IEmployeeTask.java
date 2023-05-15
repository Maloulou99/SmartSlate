package com.example.smartslate.repository;


import com.example.smartslate.model.EmployeeTask;

public interface IEmployeeTask {


    void createEmployeeTask(EmployeeTask employeeTask);
    EmployeeTask readEmployeeTask(int employeeTaskID);
    void updateEmployeeTask(EmployeeTask employeeTask);
    void deleteEmployeeTask(int employeeTaskID);
}
