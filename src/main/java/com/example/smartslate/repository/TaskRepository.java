package com.example.smartslate.repository;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
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
    public int createTask(int userID, String taskName, String description, String deadline, int projectID, int projectManagerID, String status) {
        String sql = "INSERT INTO tasks (userID, taskName, description, deadline, projectID, projectManagerID, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user_id, user_pwd);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userID);
            statement.setString(2, taskName);
            statement.setString(3, description);
            statement.setString(4, deadline);
            statement.setInt(5, projectID);
            statement.setInt(6, projectManagerID);
            statement.setString(7, status);

            statement.executeUpdate();

            System.out.println("Task created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any exceptions appropriately
        }
        return userID;
    }


    public void updateTask(Task task) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "UPDATE tasks SET projectID = ?, taskName = ?, description = ?, deadline = ?, projectManagerID = ?, status = ? WHERE taskID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, task.getProjectId());
            pstmt.setString(2, task.getTaskName());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getDeadline());
            pstmt.setInt(5, task.getProjectmanagerID());
            pstmt.setString(6, task.getStatus());
            pstmt.setInt(7, task.getTaskId());
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
            String insertQuery = "INSERT INTO employeeTasks (employeeTaskID, taskID) VALUES (?, ?)";
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

    public void associateProjectManagerWithTask(int taskId, Integer projectManagerId) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "INSERT INTO task_project_managers (taskId, projectManagerId) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, taskId);
            pstmt.setInt(2, projectManagerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<User> getEmployeesWithRoleThreeByUserId(int userId) {
        List<User> employees = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT u.* FROM users u " +
                    "INNER JOIN employeeTasks et ON u.userID = et.userID " +
                    "INNER JOIN tasks t ON et.taskID = t.taskID " +
                    "WHERE u.roleID = 3 AND t.userID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int employeeId = rs.getInt("userID");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String phoneNumber = rs.getString("phoneNumber");
                LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updatedAt").toLocalDateTime();
                int roleId = rs.getInt("roleID");

                User employee = new User(employeeId, username, password, email, firstName, lastName, phoneNumber, createdAt, updatedAt, roleId);
                employees.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employees;
    }

    public List<Task> getTasksByEmployeeId(int employeeId) {
        List<Task> tasks = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT t.* FROM tasks t " +
                    "INNER JOIN employeeTasks et ON t.taskID = et.taskID " +
                    "WHERE et.userID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int taskId = rs.getInt("taskID");
                String taskName = rs.getString("taskName");
                String description = rs.getString("description");
                String deadline = rs.getString("deadline");
                String status = rs.getString("status");
                int projectId = rs.getInt("projectID");
                int projectManagerId = rs.getInt("projectManagerID");
                int userId = rs.getInt("userID");

                Task task = new Task(taskId, taskName, description, deadline, status, projectId, projectManagerId, userId);
                tasks.add(task);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tasks;
    }

    public List<User> getEmployeesWithRoleThree() {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String query = "SELECT * FROM users WHERE roleID = 3";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            List<User> employees = new ArrayList<>();
            while (rs.next()) {
                int userId = rs.getInt("userID");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                LocalDateTime createdAt = rs.getDate("createdAt").toLocalDate().atStartOfDay();
                LocalDateTime updatedAt = rs.getDate("updatedAt").toLocalDate().atStartOfDay();
                String phoneNumber = rs.getString("phoneNumber");
                int roleId = rs.getInt("roleID");

                User employee = new User(userId, username, password, email, firstName, lastName, phoneNumber, createdAt, updatedAt, roleId);
                employees.add(employee);
            }

            return employees;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getProjectIdByTaskId(int taskId) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String sql = "SELECT projectID FROM tasks WHERE taskID = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("projectID");
            } else {
                // Handle case where taskId is not found
                throw new IllegalArgumentException("Invalid taskId: " + taskId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
