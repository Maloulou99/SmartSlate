package com.example.smartslate.repository;

import com.example.smartslate.model.EmployeeTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class EmployeeTaskRepository implements IEmployeeTask {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;


    public void createEmployeeTask(EmployeeTask employeeTask) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd);
             PreparedStatement pstmt = con.prepareStatement(
                     "INSERT INTO employee_tasks (employeeID, taskID, hours) VALUES (?, ?, ?)")) {
            pstmt.setInt(1, employeeTask.getEmployeeTaskID());
            pstmt.setInt(2, employeeTask.getTaskEmployeeID());
            pstmt.setString(3, employeeTask.getHours());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while creating employee task: " + e.getMessage());
        }
    }

    public EmployeeTask readEmployeeTask(int employeeTaskID) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd);
             PreparedStatement pstmt = con.prepareStatement(
                     "SELECT * FROM employee_tasks WHERE employeeTaskID=?")) {
            pstmt.setInt(1, employeeTaskID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                EmployeeTask employeeTask = new EmployeeTask();
                employeeTask.setEmployeeTaskID(rs.getInt("employeeTaskID"));
                employeeTask.setTaskEmployeeID(rs.getInt("taskEmployeeID"));
                employeeTask.setTaskID(rs.getInt("taskID"));
                employeeTask.setHours(rs.getString("hours"));
                return employeeTask;
            } else {
                System.out.println("Employee task with employeeTaskID=" + employeeTaskID + " not found in database.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error while reading employee task: " + e.getMessage());
            return null;
        }
    }

    public void updateEmployeeTask(EmployeeTask employeeTask) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd);
             PreparedStatement pstmt = con.prepareStatement(
                     "UPDATE employee_tasks SET taskEmployeeID=?, taskID=?, hours=? WHERE employeeTaskID=?")) {
            pstmt.setInt(1, employeeTask.getTaskEmployeeID());
            pstmt.setInt(2, employeeTask.getTaskID());
            pstmt.setString(3, employeeTask.getHours());
            pstmt.setInt(4, employeeTask.getEmployeeTaskID());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("Employee task with employeeTaskID=" + employeeTask.getEmployeeTaskID() + " not found in database.");
            } else {
                System.out.println("Employee task with employeeTaskID=" + employeeTask.getEmployeeTaskID() + " updated successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error while updating employee task: " + e.getMessage());
        }
    }

    public void deleteEmployeeTask(int employeeTaskID) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd);
             PreparedStatement pstmt = con.prepareStatement(
                     "DELETE FROM employee_tasks WHERE employeeTaskID=?")) {
            pstmt.setInt(1, employeeTaskID);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted == 0) {
                System.out.println("Employee task with employeeTaskID=" + employeeTaskID + " not found in database.");
            } else {
                System.out.println("Employee task with employeeTaskID=" + employeeTaskID + " deleted successfully.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}