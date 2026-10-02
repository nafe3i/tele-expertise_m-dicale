package ma.youcode.clinic.dao.impl;

import ma.youcode.clinic.config.DatabaseConfig;
import ma.youcode.clinic.dao.UserDAO;
import ma.youcode.clinic.entity.Role;
import ma.youcode.clinic.entity.User;

import java.sql.*;
import java.util.Optional;

public class JdbcUserDAO implements UserDAO {

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setRole(Role.valueOf(rs.getString("role")));
                    
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) user.setCreatedAt(ts.toLocalDateTime());
                    
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}