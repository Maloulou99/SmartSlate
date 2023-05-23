package com.example.smartslate.repository;


import com.example.smartslate.model.EmployeeTask;
import com.example.smartslate.model.Task;

import java.util.List;

public interface IEmployeeTask {


    List<Task> getEmployeeTasksByUserId(int employeeID);
    String calculateTotalTimeSpent(List<Task> tasks);
    String formatTotalTime(double totalTime);
}
