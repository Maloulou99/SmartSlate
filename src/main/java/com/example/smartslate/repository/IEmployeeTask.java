package com.example.smartslate.repository;


import com.example.smartslate.model.EmployeeTask;
import com.example.smartslate.model.Task;

import java.util.List;

public interface IEmployeeTask {


    void createEmployeeTask(EmployeeTask employeeTask);
    EmployeeTask readEmployeeTask(int employeeTaskID);
    void updateEmployeeTask(EmployeeTask employeeTask);
    void deleteEmployeeTask(int employeeTaskID);
    List<Task> getEmployeeTasksByUserId(int employeeID);
}
