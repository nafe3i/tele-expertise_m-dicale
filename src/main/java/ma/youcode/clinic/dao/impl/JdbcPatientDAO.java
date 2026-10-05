package ma.youcode.clinic.dao.impl;


import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import ma.youcode.clinic.dao.PatientDAO;
import ma.youcode.clinic.entity.Patient;

public class JdbcPatientDAO implements PatientDAO {
    private final DataSource dataSource;
    private static final String INSERT_SQL = """
    INSERT INTO patients (
        last_name,
        first_name,
        birth_date,
        social_security_number,
        blood_pressure,
        heart_rate,
        temperature,
        respiratory_rate
    )
    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;
    //dependency injection avec constructeur
    public JdbcPatientDAO(DataSource dataSource){
        this.dataSource = dataSource;
    }
    @Override
    public Patient save(Patient patient){
        try (
    Connection connection = dataSource.getConnection();
    PreparedStatement statement = connection.prepareStatement(
        INSERT_SQL,
        Statement.RETURN_GENERATED_KEYS
    )
) {
statement.setString(1, patient.getLastName());
statement.setString(2, patient.getFirstName());
statement.setDate(3, Date.valueOf(patient.getBirthDate()));
statement.setString(4, patient.getSocialSecurityNumber());
statement.setString(5, patient.getBloodPressure());
statement.setInt(6, patient.getHeartRate());
statement.setBigDecimal(7, patient.getTemperature());
statement.setInt(8, patient.getRespiratoryRate());
int affectedRows = statement.executeUpdate();


}

    }
}
