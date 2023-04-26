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
                project.setTitle(rs.getString("title"));
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

}
