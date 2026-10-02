package ma.youcode.clinic.service;

import ma.youcode.clinic.dao.UserDAO;
import ma.youcode.clinic.entity.User;
import ma.youcode.clinic.web.security.AuthenticatedUser;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public AuthenticatedUser authenticate(String username, String password) {
        if (isBlank(username) || isBlank(password)) {
            throw new AuthenticationException();
        }
        User user = userDAO.findByUsername(username.trim()).orElseThrow(AuthenticationException::new);
        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            throw new AuthenticationException();
        }
        return new AuthenticatedUser(user.getId(), user.getUsername(), user.getRole());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
