package com.example.smartslate.repository;

import com.example.smartslate.model.Subtask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SubTaskRepository implements ISubTask {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;

    public void createSubTask(Subtask subtask) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd);
             PreparedStatement pstmt = con.prepareStatement(
                     "INSERT INTO subtasks (taskID, subtaskName, description, startDate, endDate, budget, status) VALUES (?, ?, ?, ?, ?, ?, ?)");) {
            pstmt.setInt(1, subtask.getTaskID());
            pstmt.setString(2, subtask.getSubtaskName());
            pstmt.setString(3, subtask.getDescription());
            pstmt.setDate(4, subtask.getStartDate());
            pstmt.setDate(5, subtask.getEndDate());
            pstmt.setString(6, subtask.getBudget());
            pstmt.setString(7, subtask.getStatus());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Subtask getSubtaskById(int subtaskID) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd);
             PreparedStatement pstmt = con.prepareStatement("SELECT * FROM subtasks WHERE subtaskID = ?");) {
            pstmt.setInt(1, subtaskID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Subtask subtask = new Subtask(rs.getInt("taskID"), rs.getString("subtaskName"),
                            rs.getString("description"), rs.getDate("startDate"), rs.getDate("endDate"),
                            rs.getString("budget"), rs.getString("status"));
                    subtask.setSubtaskID(rs.getInt("subtaskID"));
                    return subtask;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSubtask(Subtask subtask) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd);
             PreparedStatement pstmt = con.prepareStatement(
                     "UPDATE subtasks SET taskID = ?, subtaskName = ?, description = ?, startDate = ?, endDate = ?, budget = ?, status = ? WHERE subtaskID = ?")) {
            pstmt.setInt(1, subtask.getTaskID());
            pstmt.setString(2, subtask.getSubtaskName());
            pstmt.setString(3, subtask.getDescription());
            pstmt.setDate(4, subtask.getStartDate());
            pstmt.setDate(5, subtask.getEndDate());
            pstmt.setString(6, subtask.getBudget());
            pstmt.setString(7, subtask.getStatus());
            pstmt.setInt(8, subtask.getSubtaskID());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteSubtask(int subtaskId) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd);
             PreparedStatement pstmtFindTaskId = con.prepareStatement(
                     "SELECT taskID FROM subtasks WHERE subtaskID=?");
             PreparedStatement pstmtDelete = con.prepareStatement(
                     "DELETE FROM subtasks WHERE subtaskID=?")) {

            // Find task ID
            pstmtFindTaskId.setInt(1, subtaskId);
            ResultSet rs = pstmtFindTaskId.executeQuery();
            boolean subtaskFound = rs.next();
            int taskId = subtaskFound ? rs.getInt(1) : 0;

            // Delete subtask
            pstmtDelete.setInt(1, subtaskId);
            int rowsDeleted = pstmtDelete.executeUpdate();
            if (rowsDeleted == 0) {
                System.out.println("Subtask with subtaskID=" + subtaskId + " not found in database.");
            } else {
                System.out.println("Subtask with subtaskID=" + subtaskId + " deleted successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error while deleting subtask: " + e.getMessage());
        }
    }

    public List<Subtask> getAllSubtasks() {
        List<Subtask> subtasks = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM subtasks");) {
            while (rs.next()) {
                Subtask subtask = new Subtask(rs.getInt("taskID"), rs.getString("subtaskName"),
                        rs.getString("description"), rs.getDate("startDate"), rs.getDate("endDate"),
                        rs.getString("budget"), rs.getString("status"));
                subtask.setSubtaskID(rs.getInt("subtaskID"));
                subtasks.add(subtask);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return subtasks;
    }

}
