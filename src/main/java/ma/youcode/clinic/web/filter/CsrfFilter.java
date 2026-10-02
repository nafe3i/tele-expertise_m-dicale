package ma.youcode.clinic.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.youcode.clinic.web.security.SessionAttributes;

import java.io.IOException;

@WebFilter("/*")
public class CsrfFilter implements Filter {
    @Override
    public void doFilter(jakarta.servlet.ServletRequest servletRequest,
                         jakarta.servlet.ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = request.getSession(false);
        String expectedToken = session == null ? null : (String) session.getAttribute(SessionAttributes.CSRF_TOKEN);
        String submittedToken = request.getParameter("_csrf");
        if (expectedToken == null || !expectedToken.equals(submittedToken)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Jeton CSRF invalide.");
            return;
        }
        chain.doFilter(request, response);
    }
}
