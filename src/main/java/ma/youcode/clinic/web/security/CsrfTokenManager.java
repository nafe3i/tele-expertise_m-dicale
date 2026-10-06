package ma.youcode.clinic.web.security;

import jakarta.servlet.http.HttpSession;

import java.security.SecureRandom;
import java.util.Base64;

public class CsrfTokenManager {
    private static final SecureRandom RANDOM = new SecureRandom();

    public String getOrCreate(HttpSession session) {
        Object token = session.getAttribute(SessionAttributes.CSRF_TOKEN);
        if (token instanceof String) {
            return (String) token;
        }
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        String generatedToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        session.setAttribute(SessionAttributes.CSRF_TOKEN, generatedToken);
        return generatedToken;
    }
}
