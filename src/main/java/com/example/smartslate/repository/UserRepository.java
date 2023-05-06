package com.example.smartslate.repository;

import com.example.smartslate.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;


    public int createUser(User newUser) {
        int userId = 0;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "INSERT INTO users (username, FirstName, Lastname, Email, Password, phonenumber, role) values (?,?,?,?,?, ?,?);";
            PreparedStatement pstmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, newUser.getUserName());
            pstmt.setString(2, newUser.getFirstName());
            pstmt.setString(3, newUser.getLastName());
            pstmt.setString(4, newUser.getEmail());
            pstmt.setString(5, newUser.getPassword());
            pstmt.setString(6, newUser.getPhoneNumber());
            pstmt.setString(7, newUser.getRole());
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

    public User getUser(int uid) {
        User user = new User();
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM users WHERE UserID = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, uid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user.setUserId(rs.getInt("UserID"));
                user.setUserName(rs.getString("UserName"));
                user.setFirstName(rs.getString("Firstname"));
                user.setLastName(rs.getString("Lastname"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("Password"));
                user.setPhoneNumber(rs.getString("PhoneNumber"));
                user.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime().toLocalDate());
            }

            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateUser(User updatedUser) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "UPDATE Users SET Username = ?, Password = ?, Email = ?, FirstName = ?, " +
                    "LastName = ?, PhoneNumber = ?, UpdatedAt = CURRENT_TIMESTAMP WHERE UserID = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, updatedUser.getUserName());
            pstmt.setString(2, updatedUser.getPassword());
            pstmt.setString(3, updatedUser.getEmail());
            pstmt.setString(4, updatedUser.getFirstName());
            pstmt.setString(5, updatedUser.getLastName());
            pstmt.setString(6, updatedUser.getPhoneNumber());
            pstmt.setInt(7, updatedUser.getUserId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(int userId) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "DELETE FROM Users WHERE UserID = ?;";
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
            String SQL = "SELECT * FROM Users;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("UserID"));
                user.setUserName(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setEmail(rs.getString("Email"));
                user.setPhoneNumber(rs.getString("PhoneNumber"));
                user.setRole(rs.getString("Role"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime().toLocalDate());
                user.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime().toLocalDate());
                users.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }


}
