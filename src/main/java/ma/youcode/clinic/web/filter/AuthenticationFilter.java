package ma.youcode.clinic.web.filter;

import ma.youcode.clinic.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Laisser passer la page de login, les assets et le servlet d'auth
        if (path.startsWith("/login") || path.startsWith("/auth") || path.startsWith("/assets/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User loggedUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (loggedUser == null) {
            res.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Contrôle d'accès par rôle
        if (path.startsWith("/infirmier") && !"INFIRMIER".equals(loggedUser.getRole().name())) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux infirmiers.");
            return;
        }

        if (path.startsWith("/medecin") && !"MEDECIN".equals(loggedUser.getRole().name())) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux médecins.");
            return;
        }

        chain.doFilter(request, response);
    }
}