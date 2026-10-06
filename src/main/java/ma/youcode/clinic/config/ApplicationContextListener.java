package ma.youcode.clinic.config;

import javax.sql.DataSource;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ma.youcode.clinic.dao.ConsultationDAO;
import ma.youcode.clinic.dao.PatientDAO;
import ma.youcode.clinic.dao.UserDAO;
import ma.youcode.clinic.dao.impl.JdbcConsultationDAO;
import ma.youcode.clinic.dao.impl.JdbcPatientDAO;
import ma.youcode.clinic.dao.impl.JdbcUserDAO;
import ma.youcode.clinic.service.ConsultationService;
import ma.youcode.clinic.service.PatientService;
import ma.youcode.clinic.service.UserService;

@WebListener
public class ApplicationContextListener implements ServletContextListener {

    public static final String USER_SERVICE_ATTRIBUTE = "userService";
    public static final String PATIENT_SERVICE_ATTRIBUTE = "patientService";
    public static final String CONSULTATION_SERVICE_ATTRIBUTE = "consultationService";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        DataSource dataSource
                = new DatabaseConfig().getDataSource();

        UserDAO userDAO
                = new JdbcUserDAO(dataSource);

        PatientDAO patientDAO
                = new JdbcPatientDAO(dataSource);

        ConsultationDAO consultationDAO
                = new JdbcConsultationDAO(dataSource);

        UserService userService
                = new UserService(userDAO);

        PatientService patientService
                = new PatientService(
                        patientDAO,
                        consultationDAO
                );

        ConsultationService consultationService
                = new ConsultationService(
                        consultationDAO,
                        patientDAO
                );

        event.getServletContext().setAttribute(
                USER_SERVICE_ATTRIBUTE,
                userService
        );

        event.getServletContext().setAttribute(
                PATIENT_SERVICE_ATTRIBUTE,
                patientService
        );

        event.getServletContext().setAttribute(
                CONSULTATION_SERVICE_ATTRIBUTE,
                consultationService
        );
    }
}
