package com.example.smartslate.repository;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TaskRepository {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;
    private StringBuilder userNamesBuilder = new StringBuilder();
    private UserRepository userRepository;
    private ProjectRepository projectRepository;

    public TaskRepository( UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    // Create a task
    public int createTask(Task task) {
        int taskId = 0;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "INSERT INTO Tasks (projectID, Description, Deadline, AssignedTo, Status)"
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, task.getProjectId());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getDeadline());
            pstmt.setObject(4, task.getAssignedTo());
            pstmt.setString(5, task.getStatus());
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


    // Read
    public List<Task> getTasksByProjectId(int projectId) {
        List<Task> tasks = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT t.taskID, t.description, t.deadline, t.status, u.username AS assignedTo, p.projectManagerID " +
                    "FROM tasks t " +
                    "JOIN users u ON u.userID = t.assignedTo " +
                    "JOIN projects p ON p.projectID = t.projectID " +
                    "WHERE t.projectID = ? AND p.projectID = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, projectId);
            pstmt.setInt(2, projectId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("taskID"));
                task.setDescription(rs.getString("description"));
                task.setDeadline(rs.getString("deadline"));
                task.setStatus(rs.getString("status"));
                User assignedTo = new User();
                assignedTo.setUsername(rs.getString("assignedTo"));
                task.setAssignedTo("assignedTo");
                Project project = new Project();
                project.setProjectId(projectId);
                project.setProjectManagerId(rs.getInt("projectManagerID"));
                task.setProject(project);
                tasks.add(task);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tasks;
    }


    // Update
    public void updateTask(Task task) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "UPDATE Tasks SET ProjectID = ?, Description = ?, Deadline = ?, AssignedTo = ?, Status = ? "
                    + "WHERE TaskID = ? AND UserID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, task.getProjectId());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getDeadline());
            pstmt.setString(4, task.getAssignedTo());
            pstmt.setString(5, task.getStatus());
            pstmt.setInt(6, task.getTaskId());
            pstmt.setInt(7, task.getUserId());
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
            String SQL = "SELECT * FROM Tasks;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("TaskID"));
                task.setProjectId(rs.getInt("ProjectID"));
                task.setDescription(rs.getString("Description"));
                task.setDeadline(rs.getString("Deadline"));
                task.setAssignedTo(rs.getString("AssignedTo"));
                task.setStatus(rs.getString("Status"));
                task.setUserId(rs.getInt("UserID"));
                tasks.add(task);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tasks;
    }


    public List<Task> getTasksByAssignedUser(String assignedUser) {
        List<Task> tasks = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM Tasks WHERE AssignedTo = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, assignedUser);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("TaskID"));
                task.setProjectId(rs.getInt("ProjectID"));
                task.setDescription(rs.getString("Description"));
                task.setDeadline(rs.getString("Deadline"));
                task.setAssignedTo(rs.getString("assignedTo"));
                task.setStatus(rs.getString("Status"));
                task.setUserId(rs.getInt("UserID"));
                tasks.add(task);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tasks;
    }


    public int getTaskById(int taskId) {
        Task task = null;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM Tasks WHERE TaskID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, taskId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                task = new Task();
                task.setTaskId(rs.getInt("TaskID"));
                task.setProjectId(rs.getInt("ProjectID"));
                task.setDescription(rs.getString("Description"));
                task.setDeadline(rs.getString("Deadline"));
                task.setAssignedTo(rs.getString("assignedTo"));
                task.setStatus(rs.getString("Status"));
                task.setUserId(rs.getInt("UserID"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return taskId;
    }

}
