package ma.youcode.clinic.web.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.youcode.clinic.config.ApplicationContextListener;
import ma.youcode.clinic.service.ConsultationService;
import ma.youcode.clinic.web.security.AuthenticatedUser;
import ma.youcode.clinic.web.security.CsrfTokenManager;
import ma.youcode.clinic.web.security.SessionAttributes;

@WebServlet("/doctor/consultations")
public class ConsultationServlet extends HttpServlet {

    private final CsrfTokenManager csrfTokenManager = new CsrfTokenManager();
    private ConsultationService consultationService;

    @Override
    public void init() {
        consultationService = (ConsultationService) getServletContext().getAttribute(
                ApplicationContextListener.CONSULTATION_SERVICE_ATTRIBUTE
        );

        if (consultationService == null) {
            throw new IllegalStateException(
                    "Le service Consultation est indisponible."
            );
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        showDashboard(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        AuthenticatedUser authenticatedUser
                = getAuthenticatedUser(request);

        try {
            consultationService.closeConsultation(
                    Long.valueOf(
                            requiredParameter(
                                    request,
                                    "consultationId"
                            )
                    ),
                    authenticatedUser.getId(),
                    requiredParameter(request, "reason"),
                    requiredParameter(request, "observations"),
                    requiredParameter(request, "diagnosis"),
                    requiredParameter(
                            request,
                            "prescribedTreatment"
                    )
            );

            response.sendRedirect(
                    request.getContextPath()
                    + "/doctor/consultations?success=closed"
            );
        } catch (NumberFormatException exception) {
            request.setAttribute(
                    "errorMessage",
                    "L'identifiant de la consultation est invalide."
            );
            showDashboard(request, response);
        } catch (IllegalArgumentException
                | IllegalStateException exception) {
            request.setAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
            showDashboard(request, response);
        }
    }

    private void showDashboard(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        HttpSession session = request.getSession();

        request.setAttribute(
                "authenticatedUser",
                session.getAttribute(
                        SessionAttributes.AUTHENTICATED_USER
                )
        );
        request.setAttribute(
                "csrfToken",
                csrfTokenManager.getOrCreate(session)
        );
        request.setAttribute(
                "pendingConsultations",
                consultationService.findPendingToday()
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/auth/dashboard-medecin.jsp"
        ).forward(request, response);
    }

    private AuthenticatedUser getAuthenticatedUser(
            HttpServletRequest request
    ) {
        return (AuthenticatedUser) request
                .getSession(false)
                .getAttribute(
                        SessionAttributes.AUTHENTICATED_USER
                );
    }

    private String requiredParameter(
            HttpServletRequest request,
            String name
    ) {
        String value = request.getParameter(name);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Tous les champs de la consultation sont obligatoires."
            );
        }

        return value.trim();
    }
}
