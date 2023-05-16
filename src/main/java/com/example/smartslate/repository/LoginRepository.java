package com.example.smartslate.repository;

import com.example.smartslate.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class LoginRepository implements ILoginRepository {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;


    public User findByUsernameOrEmailAndPassword(String usernameOrEmail, String password) {
        User user = null;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM users WHERE (Username = ? OR Email = ?) AND Password = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, usernameOrEmail);
            pstmt.setString(2, usernameOrEmail);
            pstmt.setString(3, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int roleID = rs.getInt("roleID");
                if (roleID == 1) {
                    // Admin
                    String adminSQL = "SELECT roleID FROM users WHERE roleID = ?";
                    PreparedStatement adminPstmt = con.prepareStatement(adminSQL);
                    adminPstmt.setInt(1, rs.getInt("userID"));
                    ResultSet adminRs = adminPstmt.executeQuery();

                    if (adminRs.next()) {
                        user = new User();
                        user.setUserID(rs.getInt("userID"));
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        user.setPassword(rs.getString("password"));
                        user.setFirstName(rs.getString("firstName"));
                        user.setLastName(rs.getString("lastName"));
                        user.setPhoneNumber(rs.getString("phoneNumber"));
                        user.setCreatedAt(rs.getDate("createdAt").toLocalDate().atStartOfDay());
                        user.setUpdatedAt(rs.getDate("updatedAt").toLocalDate().atStartOfDay());
                        user.setRoleID(rs.getInt("roleID"));
                    }
                } else if (roleID == 2) {
                    // Employee
                    String employeeSQL = "SELECT roleID FROM users WHERE roleID = ?";
                    PreparedStatement employeePstmt = con.prepareStatement(employeeSQL);
                    employeePstmt.setInt(1, rs.getInt("userID"));
                    ResultSet employeeRs = employeePstmt.executeQuery();

                    if (employeeRs.next()) {
                        user = new User();
                        user.setUserID(rs.getInt("userID"));
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        user.setPassword(rs.getString("password"));
                        user.setFirstName(rs.getString("firstName"));
                        user.setLastName(rs.getString("lastName"));
                        user.setPhoneNumber(rs.getString("phoneNumber"));
                        user.setCreatedAt(rs.getDate("createdAt").toLocalDate().atStartOfDay());
                        user.setUpdatedAt(rs.getDate("updatedAt").toLocalDate().atStartOfDay());
                        user.setRoleID(rs.getInt("roleID"));
                    }
                } else if (roleID == 3) {
                    System.out.println(roleID);
                    // Project Manager
                    String managerSQL = "SELECT roleID FROM users WHERE roleID = ?";
                    PreparedStatement managerPstmt = con.prepareStatement(managerSQL);
                    managerPstmt.setInt(1, rs.getInt("userID"));
                    ResultSet managerRs = managerPstmt.executeQuery();

                    if (managerRs.next()) {
                        user = new User();
                        user.setUserID(rs.getInt("userID"));
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        user.setPassword(rs.getString("password"));
                        user.setFirstName(rs.getString("firstName"));
                        user.setLastName(rs.getString("lastName"));
                        user.setPhoneNumber(rs.getString("phoneNumber"));
                        user.setCreatedAt(rs.getDate("createdAt").toLocalDate().atStartOfDay());
                        user.setUpdatedAt(rs.getDate("updatedAt").toLocalDate().atStartOfDay());
                        user.setRoleID(rs.getInt("roleID"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}