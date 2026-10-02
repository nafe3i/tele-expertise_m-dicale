package ma.youcode.clinic.dao.impl;

import ma.youcode.clinic.dao.DataAccessException;
import ma.youcode.clinic.dao.UserDAO;
import ma.youcode.clinic.entity.Role;
import ma.youcode.clinic.entity.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

public class JdbcUserDAO implements UserDAO {

    private static final String FIND_BY_USERNAME_SQL =
        "SELECT id, username, password_hash, role, created_at FROM users WHERE username = ?";

    private final DataSource dataSource;

    public JdbcUserDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME_SQL)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapUser(resultSet)) : Optional.empty();
            }
        } catch (SQLException exception) {
            throw new DataAccessException("Impossible de rechercher l'utilisateur.", exception);
        }
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        return new User(resultSet.getLong("id"), resultSet.getString("username"),
            resultSet.getString("password_hash"), Role.valueOf(resultSet.getString("role")),
            createdAt == null ? null : createdAt.toLocalDateTime());
    }
}
