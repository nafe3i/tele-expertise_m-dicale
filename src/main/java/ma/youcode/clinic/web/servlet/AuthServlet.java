package ma.youcode.clinic.web.servlet;

import ma.youcode.clinic.entity.User;
import ma.youcode.clinic.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/auth/login")
public class AuthServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = userService.authenticate(username, password);

        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);

            if ("INFIRMIER".equals(user.getRole().name())) {
                resp.sendRedirect(req.getContextPath() + "/infirmier/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/medecin/dashboard");
            }
        } else {
            req.setAttribute("error", "Nom d'utilisateur ou mot de passe incorrect.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}