package com.example.smartslate.repository;

import com.example.smartslate.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository implements IUserRepository{
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;

    public int createUser(User newUser) {
        int userId = 0;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "INSERT INTO users (username, firstName, lastName, email, password, phoneNumber, roleID) values (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement pstmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, newUser.getUsername());
            pstmt.setString(2, newUser.getFirstName());
            pstmt.setString(3, newUser.getLastName());
            pstmt.setString(4, newUser.getEmail());
            pstmt.setString(5, newUser.getPassword());
            pstmt.setString(6, newUser.getPhoneNumber());
            pstmt.setInt(7, newUser.getRoleID());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                userId = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userId;
    }

    public User getUser(int userId) {
        User user = null;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM users WHERE userID = ?;";
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

    public void updateUser(User updatedUser) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "UPDATE users SET username = ?, password = ?, email = ?, firstName = ?, " +
                    "lastName = ?, phoneNumber = ?, updatedAt = CURRENT_TIMESTAMP, roleID = ? WHERE userID = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, updatedUser.getUsername());
            pstmt.setString(2, updatedUser.getPassword());
            pstmt.setString(3, updatedUser.getEmail());
            pstmt.setString(4, updatedUser.getFirstName());
            pstmt.setString(5, updatedUser.getLastName());
            pstmt.setString(6, updatedUser.getPhoneNumber());
            pstmt.setInt(7, updatedUser.getRoleID());
            pstmt.setInt(8, updatedUser.getUserID());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(int userId) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "DELETE FROM users WHERE userID = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM users";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("userID"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                user.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                user.setRoleID(rs.getInt("roleID"));
                users.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    public List<User> getProjectManagersByProjectId(int projectId) {
        List<User> projectManagers = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT u.* FROM users u INNER JOIN projects p ON u.userID = p.projectManagerID WHERE p.projectID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("userID"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                user.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                user.setRoleID(rs.getInt("roleID"));
                projectManagers.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return projectManagers;
    }


    public String getRoleName(int roleID){
        String roleName = null;
        try(Connection con = DriverManager.getConnection(url, user_id, user_pwd)){
            String sql = "SELECT RoleName FROM ROLES WHERE RoleID = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, roleID);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                roleName = rs.getString("RoleName");
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return roleName;
    }

    public List<User> getAllProjectManagersByID() {
        List<User> projectManagers = new ArrayList<>();
        String query = "SELECT * FROM users WHERE roleID = 2";
        try(Connection con = DriverManager.getConnection(url, user_id, user_pwd)){
            PreparedStatement pr = con.prepareStatement(query);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("userID"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                user.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
                user.setRoleID(rs.getInt("roleID"));
                projectManagers.add(user);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return projectManagers;
    }

    public User getProjectManagerById(int projectManagerId) {
        User projectManager = null;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM users WHERE userID = ? AND roleID = 2;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, projectManagerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                projectManager = new User();
                projectManager.setUserID(rs.getInt("userID"));
                projectManager.setUsername(rs.getString("username"));
                projectManager.setFirstName(rs.getString("firstName"));
                projectManager.setLastName(rs.getString("lastName"));
                projectManager.setEmail(rs.getString("email"));
                projectManager.setPassword(rs.getString("password"));
                projectManager.setPhoneNumber(rs.getString("phoneNumber"));
                projectManager.setRoleID(rs.getInt("roleID"));
                projectManager.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                projectManager.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return projectManager;
    }

    public User getProjectManagerByProjectId(int projectId) {
        User projectManager = null;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM users WHERE userID IN ( SELECT projectManagerID FROM projects WHERE projectID = ?)";

            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                projectManager = new User();
                projectManager.setUserID(rs.getInt("userID"));
                projectManager.setUsername(rs.getString("username"));
                projectManager.setFirstName(rs.getString("firstName"));
                projectManager.setLastName(rs.getString("lastName"));
                projectManager.setEmail(rs.getString("email"));
                projectManager.setPassword(rs.getString("password"));
                projectManager.setPhoneNumber(rs.getString("phoneNumber"));
                projectManager.setRoleID(rs.getInt("roleID"));
                projectManager.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                projectManager.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return projectManager;
    }


}
