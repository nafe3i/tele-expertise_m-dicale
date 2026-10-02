package ma.youcode.clinic.dao.impl;

import ma.youcode.clinic.config.DatabaseConfig;
import ma.youcode.clinic.dao.PatientDAO;
import ma.youcode.clinic.entity.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPatientDAO implements PatientDAO {

    @Override
    public Patient save(Patient patient) {
        String sql = "INSERT INTO patients (last_name, first_name, birth_date, social_security_number, " +
                     "blood_pressure, heart_rate, temperature, respiratory_rate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, patient.getLastName());
            stmt.setString(2, patient.getFirstName());
            stmt.setDate(3, Date.valueOf(patient.getBirthDate()));
            stmt.setString(4, patient.getSocialSecurityNumber());
            stmt.setString(5, patient.getBloodPressure());
            stmt.setInt(6, patient.getHeartRate());
            stmt.setDouble(7, patient.getTemperature());
            stmt.setInt(8, patient.getRespiratoryRate());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    patient.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }

    @Override
    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY arrived_at DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    @Override
    public Optional<Patient> findById(Long id) {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPatient(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setId(rs.getLong("id"));
        p.setLastName(rs.getString("last_name"));
        p.setFirstName(rs.getString("first_name"));
        p.setBirthDate(rs.getDate("birth_date").toLocalDate());
        p.setSocialSecurityNumber(rs.getString("social_security_number"));
        p.setBloodPressure(rs.getString("blood_pressure"));
        p.setHeartRate(rs.getInt("heart_rate"));
        p.setTemperature(rs.getDouble("temperature"));
        p.setRespiratoryRate(rs.getInt("respiratory_rate"));
        
        Timestamp ts = rs.getTimestamp("arrived_at");
        if (ts != null) p.setArrivedAt(ts.toLocalDateTime());
        
        return p;
    }

    @Override
    public Optional<Patient> findBySocialSecurityNumber(String socialSecurityNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBySocialSecurityNumber'");
    }
}