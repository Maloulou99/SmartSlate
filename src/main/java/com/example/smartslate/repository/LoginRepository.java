package com.example.smartslate.repository;
import com.example.smartslate.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
@Repository
public class LoginRepository {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;

    public User checkUser(String email, String password) {
        User user = null;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM users WHERE Email = ? AND Password = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("Password"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }


    public User findByEmailAndPassword(String email, String password) {
        User user = null;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM users WHERE Email = ? AND Password = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("Password"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public User findByUsernameAndPassword(String username, String password) {
        User user = null;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM users WHERE username = ? AND Password = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

}
