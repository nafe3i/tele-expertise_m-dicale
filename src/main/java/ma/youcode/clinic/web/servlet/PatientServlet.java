package ma.youcode.clinic.web.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import ma.youcode.clinic.config.ApplicationContextListener;
import ma.youcode.clinic.service.PatientService;

@WebServlet("/nurse/patients")
public class PatientServlet extends HttpServlet {

    private PatientService patientService;

    @Override
    public void init() {
        patientService = (PatientService) getServletContext().getAttribute(
                ApplicationContextListener.PATIENT_SERVICE_ATTRIBUTE
        );

        if (patientService == null) {
            throw new IllegalStateException(
                    "Le service Patient est indisponible."
            );
        }
    }
}
