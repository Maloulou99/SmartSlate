package com.example.smartslate.repository;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

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
    private UserRepository userRepository;
    private ProjectRepository projectRepository;

    public TaskRepository(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    // Create a task
    public int createTask(Task task, int projectId, int projectManagerId) {
        int taskId = 0;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "INSERT INTO tasks (projectID, taskName, description, deadline, projectManagerID, status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, projectId);
            pstmt.setString(2, task.getTaskName());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getDeadline());
            pstmt.setInt(5, projectManagerId);
            pstmt.setString(6, task.getStatus());
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



    public List<Task> getTasksByProjectId(int projectId) {
        List<Task> tasks = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT t.taskID, t.projectID, t.taskName, t.description, t.deadline, t.projectManagerID, t.status, u.userID " +
                    "FROM tasks t " +
                    "LEFT JOIN employeeTasks et ON t.taskID = et.taskID " +
                    "LEFT JOIN users u ON et.taskEmployeeID = u.userID " +
                    "WHERE t.projectID = ?;";
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
                task.setProjectManagerID(rs.getInt("projectManagerID"));
                task.setStatus(rs.getString("status"));
                task.setUserId(rs.getInt("userID"));
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


    public void updateTask(Task task) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "UPDATE tasks SET projectID = ?, taskName = ?, description = ?, deadline = ?, projectManagerID = ?, status = ? WHERE taskID = ? AND userID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, task.getProjectId());
            pstmt.setString(2, task.getTaskName());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getDeadline());
            pstmt.setInt(5, task.getProjectManagerID());
            pstmt.setString(6, task.getStatus());
            pstmt.setInt(7, task.getTaskId());
            pstmt.setInt(8, task.getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // Delete
    public void deleteTask(int taskId) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "DELETE FROM Tasks WHERE TaskID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM tasks;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("taskID"));
                task.setProjectId(rs.getInt("projectID"));
                task.setTaskName(rs.getString("taskName"));
                task.setDescription(rs.getString("description"));
                task.setDeadline(rs.getString("deadline"));
                task.setProjectManagerID(rs.getInt("projectManagerID"));
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
            String SQL = "SELECT * FROM Tasks WHERE projectManagerID = ?";
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
                task.setProjectManagerID(rs.getInt("projectManagerID"));
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
                task.setProjectManagerID(rs.getInt("projectManagerID"));
                task.setStatus(rs.getString("status"));
                task.setUserId(rs.getInt("userID"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return task;
    }

}
