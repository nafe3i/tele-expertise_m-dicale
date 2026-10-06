package ma.youcode.clinic.web.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.youcode.clinic.config.ApplicationContextListener;
import ma.youcode.clinic.entity.Patient;
import ma.youcode.clinic.service.PatientService;
import ma.youcode.clinic.web.security.AuthenticatedUser;
import ma.youcode.clinic.web.security.CsrfTokenManager;
import ma.youcode.clinic.web.security.SessionAttributes;

@WebServlet("/nurse/patients")
public class PatientServlet extends HttpServlet {

    private final CsrfTokenManager csrfTokenManager = new CsrfTokenManager();
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
        try {
            Patient patient = new Patient(
                    null,
                    requiredParameter(request, "lastName"),
                    requiredParameter(request, "firstName"),
                    LocalDate.parse(
                            requiredParameter(request, "birthDate")
                    ),
                    requiredParameter(
                            request,
                            "socialSecurityNumber"
                    ),
                    requiredParameter(request, "bloodPressure"),
                    Integer.valueOf(
                            requiredParameter(request, "heartRate")
                    ),
                    new BigDecimal(
                            requiredParameter(request, "temperature")
                    ),
                    Integer.valueOf(
                            requiredParameter(
                                    request,
                                    "respiratoryRate"
                            )
                    ),
                    null
            );

            patientService.registerArrival(patient);
            response.sendRedirect(
                    request.getContextPath()
                    + "/nurse/patients?success=registered"
            );
        } catch (DateTimeParseException
                | NumberFormatException exception) {
            request.setAttribute(
                    "errorMessage",
                    "La date et les signes vitaux doivent avoir un format valide."
            );
            showDashboard(request, response);
        } catch (IllegalArgumentException exception) {
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
                "patientsToday",
                patientService.findPatientsArrivedToday()
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/auth/dashboard-infirmier.jsp"
        ).forward(request, response);
    }

    private String requiredParameter(
            HttpServletRequest request,
            String name
    ) {
        String value = request.getParameter(name);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Tous les champs du patient sont obligatoires."
            );
        }

        return value.trim();
    }
}
