package ma.youcode.clinic.service;

import ma.youcode.clinic.dao.UserDAO;
import ma.youcode.clinic.entity.Role;
import ma.youcode.clinic.entity.User;
import ma.youcode.clinic.web.security.AuthenticatedUser;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class UserServiceTest {
    @Test
    public void authenticatesUserWithValidCredentials() {
        String password = "valid-password";
        User user = new User(1L, "infirmier1", BCrypt.hashpw(password, BCrypt.gensalt()), Role.INFIRMIER, LocalDateTime.now());
        UserService userService = new UserService(username -> Optional.of(user));

        AuthenticatedUser authenticatedUser = userService.authenticate("infirmier1", password);

        assertEquals("infirmier1", authenticatedUser.getUsername());
        assertEquals(Role.INFIRMIER, authenticatedUser.getRole());
    }

    @Test(expected = AuthenticationException.class)
    public void rejectsInvalidPassword() {
        User user = new User(1L, "infirmier1", BCrypt.hashpw("valid-password", BCrypt.gensalt()), Role.INFIRMIER, LocalDateTime.now());
        UserDAO userDAO = username -> Optional.of(user);
        new UserService(userDAO).authenticate("infirmier1", "invalid-password");
    }
}
