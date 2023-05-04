package com.example.smartslate.repository;

import com.example.smartslate.model.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectRepository {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;

    // Create a project
    public int createProject(Project project) {
        int projectId = 0;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "INSERT INTO Projects (ProjectID, UserID, Name, Description, StartDate, EndDate, Budget, Status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, project.getProjectId());
            pstmt.setInt(2, project.getUserId());
            pstmt.setString(3, project.getProjectName());
            pstmt.setString(4, project.getDescription());
            pstmt.setDate(5, Date.valueOf(project.getStartDate()));
            pstmt.setDate(6, project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null);
            pstmt.setDouble(7, project.getBudget());
            pstmt.setString(8, project.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } return projectId;
    }

    //Read
    //Denne metode henter en liste over alle projekter, der er tilknyttet en bestemt bruger (userId).
    public List<Project> getProjectsByUserId(int userId) {
        List<Project> projects = new ArrayList<>();


        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM Projects WHERE UserID = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Project project = new Project();
                project.setProjectId(rs.getInt("project_id"));
                project.setUserId(rs.getInt("user_id"));
                project.setProjectName(rs.getString("title"));
                project.setDescription(rs.getString("description"));
                project.setStartDate(rs.getDate("start_date").toLocalDate());
                project.setEndDate(rs.getDate("end_date").toLocalDate());
                projects.add(project);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return projects;
    }

    // Update
    public void updateProject(Project project) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "UPDATE Projects SET Name = ?, Description = ?, StartDate = ?, EndDate = ?, Budget = ?, Status = ? "
                    + "WHERE ProjectID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, project.getProjectName());
            pstmt.setString(2, project.getDescription());
            pstmt.setDate(3, Date.valueOf(project.getStartDate()));
            pstmt.setDate(4, project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null);
            pstmt.setDouble(5, project.getBudget());
            pstmt.setString(6, project.getStatus());
            pstmt.setInt(7, project.getProjectId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Delete
    public void deleteProject(int projectId) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "DELETE FROM Projects WHERE ProjectID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, projectId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM projects;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Project project = new Project();
                project.setProjectId(rs.getInt("projectId"));
                project.setUserId(rs.getInt("UserID"));
                project.setProjectName(rs.getString("Name"));
                project.setDescription(rs.getString("Description"));
                project.setStartDate(rs.getTimestamp("StartDate").toLocalDateTime().toLocalDate());
                project.setEndDate(rs.getTimestamp("EndDate").toLocalDateTime().toLocalDate());
                project.setBudget(rs.getDouble("Budget"));
                project.setStatus(rs.getString("Status"));
                projects.add(project);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return projects;
    }

}
