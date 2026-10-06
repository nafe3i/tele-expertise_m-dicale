package ma.youcode.clinic.web.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.youcode.clinic.entity.Role;
import ma.youcode.clinic.web.security.AuthenticatedUser;
import ma.youcode.clinic.web.security.SessionAttributes;

@WebFilter(urlPatterns = {"/nurse/*", "/doctor/*"})
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(
            jakarta.servlet.ServletRequest servletRequest,
            jakarta.servlet.ServletResponse servletResponse,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest request
                = (HttpServletRequest) servletRequest;
        HttpServletResponse response
                = (HttpServletResponse) servletResponse;

        AuthenticatedUser user = getAuthenticatedUser(request);

        if (user == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        Role requiredRole = request.getServletPath()
                .startsWith("/nurse/")
                ? Role.INFIRMIER
                : Role.GENERALISTE;

        if (user.getRole() != requiredRole) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Vous n'êtes pas autorisé à accéder à cette page."
            );
            return;
        }

        chain.doFilter(request, response);
    }

    private AuthenticatedUser getAuthenticatedUser(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object user = session.getAttribute(
                SessionAttributes.AUTHENTICATED_USER
        );

        return user instanceof AuthenticatedUser
                ? (AuthenticatedUser) user
                : null;
    }
}
