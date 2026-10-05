package ma.youcode.clinic.config;

import javax.sql.DataSource;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ma.youcode.clinic.dao.UserDAO;
import ma.youcode.clinic.dao.impl.JdbcUserDAO;
import ma.youcode.clinic.service.UserService;

@WebListener
public class ApplicationContextListener implements ServletContextListener {
    public static final String USER_SERVICE_ATTRIBUTE = "userService";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        DataSource dataSource = new DatabaseConfig().getDataSource();
        UserDAO userDAO = new JdbcUserDAO(dataSource);
        event.getServletContext().setAttribute(USER_SERVICE_ATTRIBUTE, new UserService(userDAO));
    }
}
