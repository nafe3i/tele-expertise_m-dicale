package ma.youcode.clinic.web.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.youcode.clinic.config.ApplicationContextListener;
import ma.youcode.clinic.service.AuthenticationException;
import ma.youcode.clinic.service.UserService;
import ma.youcode.clinic.entity.Role;
import ma.youcode.clinic.web.security.AuthenticatedUser;
import ma.youcode.clinic.web.security.CsrfTokenManager;
import ma.youcode.clinic.web.security.SessionAttributes;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class AuthServlet extends HttpServlet {
    private final CsrfTokenManager csrfTokenManager = new CsrfTokenManager();
    private UserService userService;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute(ApplicationContextListener.USER_SERVICE_ATTRIBUTE);
        if (userService == null) {
            throw new IllegalStateException("Le service d'authentification est indisponible.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        if (isLogoutRequest(request)) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        AuthenticatedUser user = getAuthenticatedUser(request);
        if (user != null) {
            redirectToDashboard(request, response, user);
            return;
        }
        showLoginForm(request, response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        if (isLogoutRequest(request)) {
            logout(request, response);
            return;
        }
        try {
            AuthenticatedUser user = userService.authenticate(
                request.getParameter("username"), request.getParameter("password")
            );
            HttpSession session = request.getSession();
            request.changeSessionId();
            session.setAttribute(SessionAttributes.AUTHENTICATED_USER, user);
            redirectToDashboard(request, response, user);
        } catch (AuthenticationException exception) {
            showLoginForm(request, response, exception.getMessage());
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void showLoginForm(HttpServletRequest request, HttpServletResponse response, String errorMessage)
        throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setAttribute("csrfToken", csrfTokenManager.getOrCreate(session));
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    private AuthenticatedUser getAuthenticatedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object user = session.getAttribute(SessionAttributes.AUTHENTICATED_USER);
        return user instanceof AuthenticatedUser ? (AuthenticatedUser) user : null;
    }

    private void redirectToDashboard(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticatedUser user
    ) throws IOException {
        if (user.getRole() == Role.INFIRMIER) {
            response.sendRedirect(
                    request.getContextPath() + "/nurse/patients"
            );
            return;
        }

        response.sendRedirect(
                request.getContextPath() + "/doctor/consultations"
        );
    }

    private boolean isLogoutRequest(HttpServletRequest request) {
        return "/logout".equals(request.getServletPath());
    }

}
