package ma.youcode.clinic.service;

import ma.youcode.clinic.dao.UserDAO;
import ma.youcode.clinic.dao.impl.JdbcUserDAO;
import ma.youcode.clinic.entity.User;
import ma.youcode.clinic.util.PasswordUtil;

import java.util.Optional;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new JdbcUserDAO();
    }

    public User authenticate(String username, String plainPassword) {
        Optional<User> userOpt = userDAO.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (PasswordUtil.checkPassword(plainPassword, user.getPasswordHash())) {
                return user;
            }
        }
        return null;
    }
}