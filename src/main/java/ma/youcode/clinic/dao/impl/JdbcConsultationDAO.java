package ma.youcode.clinic.dao.impl;

import ma.youcode.clinic.config.DatabaseConfig;
import ma.youcode.clinic.dao.ConsultationDAO;
import ma.youcode.clinic.entity.Consultation;
import ma.youcode.clinic.entity.ConsultationStatus;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class JdbcConsultationDAO implements ConsultationDAO {

    @Override
    public Consultation save(Consultation c) {
        String sql = "INSERT INTO consultations (patient_id, doctor_id, reason, observations, diagnosis, " +
                     "prescribed_treatment, cost, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, c.getPatientId());
            stmt.setLong(2, c.getDoctorId());
            stmt.setString(3, c.getReason());
            stmt.setString(4, c.getObservations());
            stmt.setString(5, c.getDiagnosis());
            stmt.setString(6, c.getPrescribedTreatment());
            stmt.setBigDecimal(7, c.getCost()); 
            stmt.setString(8, c.getStatus() != null ? c.getStatus().name() : ConsultationStatus.TERMINEE.name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public List<Long> findCompletedPatientIdsByDate(LocalDate date) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findCompletedPatientIdsByDate'");
    }
}