package ma.youcode.clinic.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import ma.youcode.clinic.dao.PatientDAO;
import ma.youcode.clinic.entity.Patient;

public class JdbcPatientDAO implements PatientDAO {

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

    private final DataSource dataSource;

    public JdbcPatientDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Patient save(Patient patient) {
        try (
                Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(
                INSERT_SQL,
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, patient.getLastName());
            statement.setString(2, patient.getFirstName());
            statement.setDate(3, Date.valueOf(patient.getBirthDate()));
            statement.setString(4, patient.getSocialSecurityNumber());
            statement.setString(5, patient.getBloodPressure());
            statement.setInt(6, patient.getHeartRate());
            statement.setBigDecimal(7, patient.getTemperature());
            statement.setInt(8, patient.getRespiratoryRate());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                        "Aucun patient n'a été enregistré."
                );
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    patient.setId(generatedKeys.getLong(1));
                    return patient;
                }

                throw new SQLException(
                        "Identifiant du patient non généré."
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erreur pendant l'enregistrement du patient.",
                    e
            );
        }
    }

    @Override
    public Optional<Patient> findById(Long id) {
        try (
                Connection connection = dataSource.getConnection(); PreparedStatement statement
                = connection.prepareStatement("SELECT * FROM patients WHERE id = ?")) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                Patient patient = new Patient(
                        resultSet.getLong("id"),
                        resultSet.getString("last_name"),
                        resultSet.getString("first_name"),
                        resultSet.getDate("birth_date").toLocalDate(),
                        resultSet.getString("social_security_number"),
                        resultSet.getString("blood_pressure"),
                        resultSet.getInt("heart_rate"),
                        resultSet.getBigDecimal("temperature"),
                        resultSet.getInt("respiratory_rate"),
                        resultSet.getTimestamp("arrived_at").toLocalDateTime()
                );

                return Optional.of(patient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erreur pendant la recherche du patient.",
                    e
            );
        }
    }

    @Override
    public Patient updateVitalSigns(Patient patient) {
        String sql = """
            UPDATE patients
            SET blood_pressure = ?,
                heart_rate = ?,
                temperature = ?,
                respiratory_rate = ?,
                arrived_at = CURRENT_TIMESTAMP

            WHERE id = ?
            """;

        try (
                Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, patient.getBloodPressure());
            statement.setInt(2, patient.getHeartRate());
            statement.setBigDecimal(3, patient.getTemperature());
            statement.setInt(4, patient.getRespiratoryRate());
            // statement.setTimestamp(
            //         5,
            //         Timestamp.valueOf(patient.getArrivedAt())
            // );
            statement.setLong(5, patient.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                        "Aucun patient n'a été mis à jour."
                );
            }

            return patient;
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erreur pendant la mise à jour des signes vitaux du patient.",
                    e
            );
        }
    }

    @Override

    public Optional<Patient> findBySocialSecurityNumber(String socialSecurityNumber) {

        try (
                Connection connection = dataSource.getConnection(); PreparedStatement statement
                = connection.prepareStatement("SELECT * FROM patients WHERE social_security_number = ?")) {
            statement.setString(1, socialSecurityNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                Patient patient = new Patient(
                        resultSet.getLong("id"),
                        resultSet.getString("last_name"),
                        resultSet.getString("first_name"),
                        resultSet.getDate("birth_date").toLocalDate(),
                        resultSet.getString("social_security_number"),
                        resultSet.getString("blood_pressure"),
                        resultSet.getInt("heart_rate"),
                        resultSet.getBigDecimal("temperature"),
                        resultSet.getInt("respiratory_rate"),
                        resultSet.getTimestamp("arrived_at").toLocalDateTime()
                );

                return Optional.of(patient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erreur pendant la recherche du patient par numéro de sécurité sociale.",
                    e
            );
        }
    }

    @Override
    public List<Patient> findAll() {
        try (
                Connection connection = dataSource.getConnection(); PreparedStatement statement
                = connection.prepareStatement("SELECT * FROM patients")) {

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Patient> patients = new ArrayList<>();
                while (resultSet.next()) {
                    Patient patient = new Patient(
                            resultSet.getLong("id"),
                            resultSet.getString("last_name"),
                            resultSet.getString("first_name"),
                            resultSet.getDate("birth_date").toLocalDate(),
                            resultSet.getString("social_security_number"),
                            resultSet.getString("blood_pressure"),
                            resultSet.getInt("heart_rate"),
                            resultSet.getBigDecimal("temperature"),
                            resultSet.getInt("respiratory_rate"),
                            resultSet.getTimestamp("arrived_at").toLocalDateTime()
                    );
                    patients.add(patient);
                }
                return patients;
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erreur pendant la récupération de tous les patients.",
                    e
            );
        }
    }
}
