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
                    String adminSQL = "SELECT * FROM roles " +
                            "JOIN users ON roles.roleID = users.roleID " +
                            "WHERE roles.roleID = ?";
                    PreparedStatement adminPstmt = con.prepareStatement(adminSQL);
                    adminPstmt.setInt(1, rs.getInt("roleID"));
                    ResultSet adminRs = adminPstmt.executeQuery();

                    if (adminRs.next()) {
                        user = new User();
                        user.setUserID(adminRs.getInt("userID"));
                        user.setUsername(adminRs.getString("Username"));
                        user.setEmail(adminRs.getString("Email"));
                        user.setPassword(adminRs.getString("Password"));
                        user.setFirstName(adminRs.getString("firstName")); // Include the "firstName" column
                        user.setLastName(adminRs.getString("lastName"));
                        user.setPhoneNumber(adminRs.getString("phoneNumber"));
                        user.setCreatedAt(adminRs.getDate("createdAt").toLocalDate().atStartOfDay()); // Fix the column name and convert to LocalDate
                        user.setUpdatedAt(adminRs.getDate("updatedAt").toLocalDate().atStartOfDay()); // Fix the column name and convert to LocalDate
                        user.setRoleID(adminRs.getInt("roleID"));
                    }

                } else if (roleID == 2) {
                    // Employee
                    String employeeSQL = "SELECT * FROM roles " +
                            "JOIN users ON roles.roleID = users.roleID " +
                            "WHERE roles.roleID = ?";
                    PreparedStatement employeePstmt = con.prepareStatement(employeeSQL);
                    employeePstmt.setInt(1, rs.getInt("roleID"));
                    ResultSet employeeRs = employeePstmt.executeQuery();

                    if (employeeRs.next()) {
                        user = new User();
                        user.setUserID(employeeRs.getInt("userID"));
                        user.setUsername(employeeRs.getString("Username"));
                        user.setEmail(employeeRs.getString("Email"));
                        user.setPassword(employeeRs.getString("Password"));
                        user.setFirstName(employeeRs.getString("firstName")); 
                        user.setLastName(employeeRs.getString("lastName"));
                        user.setPhoneNumber(employeeRs.getString("phoneNumber"));
                        user.setCreatedAt(employeeRs.getDate("createdAt").toLocalDate().atStartOfDay()); // Fix the column name and convert to LocalDate
                        user.setUpdatedAt(employeeRs.getDate("updatedAt").toLocalDate().atStartOfDay()); // Fix the column name and convert to LocalDate
                        user.setRoleID(employeeRs.getInt("roleID"));
                    }
                } else if (roleID == 3) {
                    // Project Manager
                    String managerSQL = "SELECT * FROM roles " +
                            "JOIN users ON roles.roleID = users.roleID " +
                            "WHERE roles.roleID = ? ";
                    PreparedStatement managerPstmt = con.prepareStatement(managerSQL);
                    managerPstmt.setInt(1, rs.getInt("roleID"));
                    ResultSet managerRs = managerPstmt.executeQuery();

                    if (managerRs.next()) {
                        user = new User();
                        user.setUserID(managerRs.getInt("userID"));
                        user.setUsername(managerRs.getString("Username"));
                        user.setEmail(managerRs.getString("Email"));
                        user.setPassword(managerRs.getString("Password"));
                        user.setFirstName(managerRs.getString("firstName"));
                        user.setLastName(managerRs.getString("lastName"));
                        user.setPhoneNumber(managerRs.getString("phoneNumber"));
                        user.setCreatedAt(managerRs.getDate("createdAt").toLocalDate().atStartOfDay()); // Fix the column name and convert to LocalDate
                        user.setUpdatedAt(managerRs.getDate("updatedAt").toLocalDate().atStartOfDay()); // Fix the column name and convert to LocalDate
                        user.setRoleID(managerRs.getInt("roleID"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

                    }