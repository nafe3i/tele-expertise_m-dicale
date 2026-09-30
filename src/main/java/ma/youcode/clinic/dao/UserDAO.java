package ma.youcode.clinic.dao;

import ma.youcode.clinic.entity.User;

import java.util.Optional;

public interface UserDAO {

    Optional<User> findByUsername(String username);
}
