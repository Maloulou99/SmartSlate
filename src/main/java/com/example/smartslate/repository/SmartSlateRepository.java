package com.example.smartslate.repository;
import com.example.smartslate.model.User;
import org.springframework.beans.factory.annotation.Value;
import java.sql.*;

public class SmartSlateRepository {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user_id;
    @Value("${spring.datasource.password}")
    String user_pwd;

    public int createUser(User newUser) {
        int userId = 0;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "INSERT INTO user (first_name, last_name, user_email, user_password) values (?,?,?,?);";
            PreparedStatement pstmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, newUser.getFirstName());
            pstmt.setString(2, newUser.getLastName());
            pstmt.setString(3, newUser.getEmail());
            pstmt.setString(4, newUser.getPassword());
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

        public User getUser(int uid){
            User user = new User();
            try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
                String SQL = "SELECT * FROM user WHERE user_id = ?;";
                PreparedStatement pstmt = con.prepareStatement(SQL);
                pstmt.setInt(1, uid);
                ResultSet rs = pstmt.executeQuery();

                if(rs.next()){
                    user.setUserId(rs.getInt("user_id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("user_email"));
                    user.setPassword(rs.getString("user_password"));
                }

                return user;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }


}
