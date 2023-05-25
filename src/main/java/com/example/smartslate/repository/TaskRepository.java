package com.example.smartslate.repository;

import com.example.smartslate.model.Project;
import com.example.smartslate.model.Task;
import com.example.smartslate.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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


    public int createTask(int userID, String taskName, String description, BigDecimal hours, int projectID, int projectManagerID, String status) {
        String sql = "INSERT INTO tasks (userID, taskName, description, hours, projectID, projectManagerID, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user_id, user_pwd);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, userID);
            statement.setString(2, taskName);
            statement.setString(3, description);
            statement.setBigDecimal(4, hours);
            statement.setInt(5, projectID);
            statement.setInt(6, projectManagerID);
            statement.setString(7, status);

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int taskId = generatedKeys.getInt(1);
                System.out.println("Task created successfully! Task ID: " + taskId);
                return taskId;
            } else {
                throw new SQLException("Creating task failed, no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any exceptions appropriately
        }
        return 0; // or handle failure case accordingly
    }



    public void updateTask(Task task) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "UPDATE tasks SET projectID = ?, taskName = ?, description = ?, hours = ?, projectManagerID = ?, status = ? WHERE taskID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, task.getProjectId());
            pstmt.setString(2, task.getTaskName());
            pstmt.setString(3, task.getDescription());
            pstmt.setBigDecimal(4, task.getHours());
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

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM tasks";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("taskID"));
                task.setProjectId(rs.getInt("projectID"));
                task.setTaskName(rs.getString("taskName"));
                task.setDescription(rs.getString("description"));
                task.setHours(rs.getBigDecimal("hours"));
                task.setProjectmanagerID(rs.getInt("userID"));
                task.setStatus(rs.getString("status"));
                tasks.add(task);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tasks;
    }
    public int getUserIdByTaskId(int taskId) {
        int userId = 0;

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT userID FROM tasks WHERE taskID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, taskId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("userID");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userId;
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
                task.setHours(rs.getBigDecimal("hours"));
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
                task.setHours(rs.getBigDecimal("hours"));
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
                task.setHours(rs.getBigDecimal("hours"));
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

    public Task getTaskByProjectId(int projectId) {
        Task task = null;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM Tasks WHERE ProjectID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                task = new Task();
                task.setTaskId(rs.getInt("taskID"));
                task.setProjectId(rs.getInt("projectID"));
                task.setTaskName(rs.getString("taskName"));
                task.setDescription(rs.getString("description"));
                task.setHours(rs.getBigDecimal("hours"));
                int projectManagerID = rs.getInt("userID");
                task.setStatus(rs.getString("userID"));
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
                task.setHours(rs.getBigDecimal("hours"));
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
            String insertQuery = "INSERT INTO employeeTasks (taskID, userID) VALUES (?, ?)";
            PreparedStatement insertStmt = con.prepareStatement(insertQuery);
            for (int employeeId : employeeIds) {
                insertStmt.setInt(1, taskId);
                insertStmt.setInt(2, employeeId);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getEmployeesWithRoleThreeByUserId(int userId) {
        List<User> employees = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT DISTINCT u.* " +
                    "FROM users u " +
                    "INNER JOIN employeeTasks et ON u.userID = et.userID " +
                    "INNER JOIN tasks t ON et.taskID = t.taskID " +
                    "WHERE u.userID = ? AND u.roleID = 3;";
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
                BigDecimal hours = rs.getBigDecimal("hours");
                String status = rs.getString("status");
                int projectId = rs.getInt("projectID");
                int projectManagerId = rs.getInt("projectManagerID");
                int userId = rs.getInt("userID");

                Task task = new Task(taskId, taskName, description, hours, status, projectId, projectManagerId, userId);
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
                throw new IllegalArgumentException("Invalid taskId: " + taskId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getEmployeesByTaskId(int taskId) {
        List<User> employees = new ArrayList<>();

        String sql = "SELECT userID FROM employeeTasks WHERE taskID = ?";
        try (Connection connection = DriverManager.getConnection(url, user_id, user_pwd);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, taskId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int employeeId = resultSet.getInt("userID");
                User employee = getUser(employeeId);
                if (employee != null) {
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    public User getUser(int userId) {
        User user = null;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM users WHERE userID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserID(rs.getInt("userID"));
                user.setUsername(rs.getString("username"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setRoleID(rs.getInt("roleID"));
                user.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                user.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }


    public List<List<String>> getListOfUserLists(List<Task> taskList) {
        List<List<String>> listOfUserLists = new ArrayList<>();
        for (Task task : taskList) {
            List<User> users = getEmployeesWithRoleThreeByUserId(task.getUserId());
            List<String> names = new ArrayList<>();
            for (User user : users) {
                String fullName = user.getFirstName() + " " + user.getLastName();
                names.add(fullName);
            }
            listOfUserLists.add(names);
        }
        return listOfUserLists;
    }


    public int getTaskIdByProjectId(int projectId) {
        int taskId = -1;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT taskId FROM tasks WHERE projectId = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                taskId = rs.getInt("taskId");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (taskId == -1) {
            throw new IllegalArgumentException("Ingen opgave fundet for projekt-id: " + projectId);
        }

        return taskId;
    }

}
