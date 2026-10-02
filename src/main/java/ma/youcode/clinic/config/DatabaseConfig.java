package ma.youcode.clinic.config;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public final class DatabaseConfig {

    public DataSource getDataSource() {
        try {
            InitialContext context = new InitialContext();

            return (DataSource) context.lookup(
                "java:comp/env/jdbc/GestionClinique"
            );
        } catch (NamingException exception) {
            throw new IllegalStateException(
                "Le DataSource jdbc/GestionClinique est introuvable.",
                exception
            );
        }
    }
}