package com.example.smartslate.repository;

import com.example.smartslate.model.EmployeeTask;
import com.example.smartslate.model.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeTaskRepository implements IEmployeeTask {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;



    public List<Task> getEmployeeTasksByUserId(int employeeID) {
        List<Task> tasks = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd);
             PreparedStatement pstmt = con.prepareStatement(
                     "SELECT t.taskID, t.taskName, t.description, t.hours, t.status, p.projectName " +
                             "FROM tasks t " +
                             "INNER JOIN employeeTasks et ON et.taskID = t.taskID " +
                             "INNER JOIN users u ON u.userID = et.userID " +
                             "INNER JOIN projects p ON t.projectID = p.projectID " +
                             "WHERE u.userID = ?")) {
            pstmt.setInt(1, employeeID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int taskID = rs.getInt("taskID");
                    String taskName = rs.getString("taskName");
                    String description = rs.getString("description");
                    double hours = rs.getDouble("hours");
                    String status = rs.getString("status");
                    String projectName = rs.getString("projectName");

                    // Create a Task object and add it to the list
                    Task task = new Task(taskID, taskName, description, hours, status, projectName);
                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return tasks;
    }


    public String formatTotalTime(double totalTime) {
        int hours = (int) totalTime;
        int minutes = (int) ((totalTime - hours) * 60);

        return String.format("%02d:%02d", hours, minutes);
    }

    public String calculateTotalTimeSpent(List<Task> tasks) {
        double totalTimeSpent = 0;

        for (Task task : tasks) {
            double hours = task.getHours();
            totalTimeSpent += hours;
        }

        return formatTotalTime(totalTimeSpent);
    }
}

