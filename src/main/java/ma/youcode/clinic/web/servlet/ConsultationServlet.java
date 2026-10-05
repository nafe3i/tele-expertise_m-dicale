package ma.youcode.clinic.web.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import ma.youcode.clinic.config.ApplicationContextListener;
import ma.youcode.clinic.service.ConsultationService;

@WebServlet("/doctor/consultations")
public class ConsultationServlet extends HttpServlet {

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
}
