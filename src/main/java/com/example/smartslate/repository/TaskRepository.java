package com.example.smartslate.repository;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.sound.midi.Soundbank;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository implements ITaskRepository {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;


    // Create a task
    public int createTask(Task task) {
        int taskId = 0;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "INSERT INTO tasks (projectID, taskName, description, deadline, projectManagerID, userID, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, task.getProjectId());
            pstmt.setString(2, task.getTaskName());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getDeadline());
            pstmt.setInt(5, task.getProjectmanagerID());
            System.out.println(task.getProjectmanagerID());
            pstmt.setInt(6, task.getUserId());
            System.out.println(task.getUserID());
            pstmt.setString(7, task.getStatus());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                taskId = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return taskId;
    }


    public void updateTask(Task task) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "UPDATE tasks SET projectID = ?, taskName = ?, description = ?, deadline = ?, projectManagerID = ?, status = ? WHERE taskID = ? AND userID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, task.getProjectId());
            pstmt.setString(2, task.getTaskName());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getDeadline());
            pstmt.setInt(5, task.getProjectmanagerID());
            pstmt.setString(6, task.getStatus());
            pstmt.setInt(7, task.getTaskId());
            pstmt.setInt(8, task.getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    // Delete one task from a project
    public void deleteTaskFromProject(int projectId, int taskId) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            // Opdater employeetasks tabellen for at fjerne relationen til tasken
            String updateSQL = "DELETE FROM employeetasks WHERE taskID = ?";
            PreparedStatement updateStmt = con.prepareStatement(updateSQL);
            updateStmt.setInt(1, taskId);
            updateStmt.executeUpdate();

            // Slet task fra tasks tabellen
            String deleteSQL = "DELETE FROM tasks WHERE taskID = ? AND projectID = ?";
            PreparedStatement deleteStmt = con.prepareStatement(deleteSQL);
            deleteStmt.setInt(1, taskId);
            deleteStmt.setInt(2, projectId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Task> getAllTasks(int userID) {
        List<Task> tasks = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM tasks WHERE userID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("taskID"));
                task.setProjectId(rs.getInt("projectID"));
                task.setTaskName(rs.getString("taskName"));
                task.setDescription(rs.getString("description"));
                task.setDeadline(rs.getString("deadline"));
                task.setProjectmanagerID(rs.getInt("userID"));
                task.setStatus(rs.getString("status"));
                tasks.add(task);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tasks;
    }

    public List<Task> getTasksByProjectManagerID(int projectManagerID) {
        List<Task> tasks = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM Tasks WHERE userID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, projectManagerID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("taskID"));
                task.setProjectId(rs.getInt("projectID"));
                task.setTaskName(rs.getString("taskName"));
                task.setDescription(rs.getString("description"));
                task.setDeadline(rs.getString("deadline"));
                task.setProjectmanagerID(rs.getInt("userID"));
                task.setStatus(rs.getString("status"));
                tasks.add(task);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tasks;
    }




    public Task getTaskById(int taskId) {
        Task task = null;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM Tasks WHERE TaskID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, taskId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                task = new Task();
                task.setTaskId(rs.getInt("taskID"));
                task.setProjectId(rs.getInt("projectID"));
                task.setTaskName(rs.getString("taskName"));
                task.setDescription(rs.getString("description"));
                task.setDeadline(rs.getString("deadline"));
                int projectManagerID = rs.getInt("userID");
                if (!rs.wasNull()) {
                    task.setProjectmanagerID(projectManagerID);
                }
                task.setStatus(rs.getString("status"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return task;
    }

    public List<Task> getTasksByProjectId(int projectId) {
        List<Task> tasks = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT t.* FROM tasks t JOIN projects p ON t.projectID = p.projectID " +
                    "JOIN users pm ON t.projectManagerID = pm.userID " +
                    "JOIN users u ON t.userID = u.userID WHERE p.projectID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("taskID"));
                task.setProjectId(rs.getInt("projectID"));
                task.setTaskName(rs.getString("taskName"));
                task.setDescription(rs.getString("description"));
                task.setDeadline(rs.getString("deadline"));
                task.setProjectmanagerID(rs.getInt("projectManagerID"));
                task.setUserId(rs.getInt("userID"));
                task.setStatus(rs.getString("status"));
                Project project = new Project();
                project.setProjectId(rs.getInt("projectID"));
                task.setProject(project);
                tasks.add(task);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tasks;
    }

    public void associateEmployeesWithTask(int taskId, List<Integer> employeeIds) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            // Slet eksisterende tilknytninger mellem opgaven og medarbejdere
            String deleteQuery = "DELETE FROM employeeTasks WHERE taskID = ?";
            PreparedStatement deleteStmt = con.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, taskId);
            deleteStmt.executeUpdate();

            // Opret nye tilknytninger mellem opgaven og valgte medarbejdere
            String insertQuery = "INSERT INTO employeeTasks (taskEmployeeID, taskID) VALUES (?, ?)";
            PreparedStatement insertStmt = con.prepareStatement(insertQuery);
            for (int employeeId : employeeIds) {
                insertStmt.setInt(1, employeeId);
                insertStmt.setInt(2, taskId);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
