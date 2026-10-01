package ma.youcode.clinic.config;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {

    private static DataSource dataSource;

    static {
        try {
            Context initContext = new InitialContext();
            Context envContext  = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/CliniqueDB");
        } catch (NamingException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur d'initialisation du DataSource JNDI", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}