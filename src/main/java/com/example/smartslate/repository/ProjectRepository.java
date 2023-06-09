package com.example.smartslate.repository;

import com.example.smartslate.model.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectRepository implements IProjectRepository{
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;

    public int createProject(Project project) {
        int projectId = 0;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "INSERT INTO projects (userID, projectName, description, startDate, endDate, budget, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, project.getUserID());
            pstmt.setString(2, project.getProjectName());
            pstmt.setString(3, project.getDescription());
            pstmt.setDate(4, Date.valueOf(project.getStartDate()));
            pstmt.setDate(5, project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null);
            pstmt.setString(6, project.getBudget());
            pstmt.setString(7, project.getStatus());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                projectId = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return projectId;
    }

    public void updateProject(Project project) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "UPDATE projects SET userID=?, projectName=?, description=?, startDate=?, endDate=?, budget=?, status=? WHERE projectID=?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, project.getUserID());
            pstmt.setString(2, project.getProjectName());
            pstmt.setString(3, project.getDescription());
            pstmt.setDate(4, Date.valueOf(project.getStartDate()));
            pstmt.setDate(5, project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null);
            pstmt.setString(6, project.getBudget());
            pstmt.setString(7, project.getStatus());
            pstmt.setInt(8, project.getProjectId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteProject(int projectId) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            try (PreparedStatement pstmt1 = con.prepareStatement("DELETE FROM employeeTasks WHERE taskID IN (SELECT taskID FROM tasks WHERE projectID = ?)")) {
                pstmt1.setInt(1, projectId);
                pstmt1.executeUpdate();
            }

            try (PreparedStatement pstmt2 = con.prepareStatement("DELETE FROM tasks WHERE projectID = ?")) {
                pstmt2.setInt(1, projectId);
                pstmt2.executeUpdate();
            }

            try (PreparedStatement pstmt3 = con.prepareStatement("DELETE FROM projects WHERE projectID = ?")) {
                pstmt3.setInt(1, projectId);
                pstmt3.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Project getProjectById(int projectId) {
        Project project = null;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM projects WHERE projectID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                project = new Project();
                project.setProjectId(rs.getInt("projectID"));
                project.setUserID(rs.getInt("userID"));
                project.setProjectName(rs.getString("projectName"));
                project.setDescription(rs.getString("description"));
                project.setStartDate(rs.getDate("startDate").toLocalDate());
                project.setEndDate(rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null);
                project.setBudget(rs.getString("budget"));
                project.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return project;
    }

        public List<Project> getAllProjects() {
            List<Project> projects = new ArrayList<>();
            try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
                String SQL = "SELECT * FROM projects";
                PreparedStatement pstmt = con.prepareStatement(SQL);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Project project = new Project();
                    project.setProjectId(rs.getInt("projectID"));
                    project.setUserID(rs.getInt("userID"));
                    project.setProjectName(rs.getString("projectName"));
                    project.setDescription(rs.getString("description"));
                    project.setStartDate(rs.getDate("startDate").toLocalDate());
                    project.setEndDate(rs.getDate("endDate").toLocalDate() != null ? rs.getDate("endDate").toLocalDate() : null);
                    project.setBudget(rs.getString("budget"));
                    project.setStatus(rs.getString("status"));
                    projects.add(project);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return projects;
        }

    public List<Project> getAllProjectsByUserId(int userId) {
        List<Project> projects = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM projects WHERE userID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Project project = new Project();
                project.setProjectId(rs.getInt("projectID"));
                project.setUserID(rs.getInt("userID"));
                project.setProjectName(rs.getString("projectName"));
                project.setDescription(rs.getString("description"));
                project.setStartDate(rs.getDate("startDate").toLocalDate());
                project.setEndDate(rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null);
                project.setBudget(rs.getString("budget"));
                project.setStatus(rs.getString("status"));
                projects.add(project);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return projects;
    }

    public int getProjectManagerIdByProjectId(int projectId) {
        int projectManagerId = 0;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT userID FROM projects WHERE projectID = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                projectManagerId = rs.getInt("userID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return projectManagerId;
    }


}
