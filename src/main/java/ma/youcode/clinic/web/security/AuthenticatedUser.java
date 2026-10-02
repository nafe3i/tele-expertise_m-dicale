package ma.youcode.clinic.web.security;

import ma.youcode.clinic.entity.Role;

public class AuthenticatedUser {
    private final Long id;
    private final String username;
    private final Role role;

    public AuthenticatedUser(Long id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public Role getRole() { return role; }
}
