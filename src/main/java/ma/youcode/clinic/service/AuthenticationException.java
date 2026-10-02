package ma.youcode.clinic.service;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("Identifiants invalides.");
    }
}
