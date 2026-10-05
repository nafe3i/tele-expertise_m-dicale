package ma.youcode.clinic.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import ma.youcode.clinic.dao.ConsultationDAO;
import ma.youcode.clinic.entity.Consultation;
import ma.youcode.clinic.entity.ConsultationStatus;

public class JdbcConsultationDAO implements ConsultationDAO {

    private static final String INSERT_SQL = """
        INSERT INTO consultations (
            patient_id,
            status
        )
        VALUES (?, ?)
        """;

    private static final String UPDATE_SQL = """
            UPDATE consultations SET doctor_id=?,reason=?,observations=?,diagnosis=?,prescribed_treatment=?,cost=?,status='TERMINEE',closed_at=CURRENT_TIMESTAMP WHERE id= ?
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT * FROM consultations WHERE id = ?
            """;
    private static final String FIND_PENDING_BY_DATE_SQL = """
        SELECT
            id,
            patient_id,
            status
        FROM consultations
        WHERE status = ?
          AND created_at >= ?
          AND created_at < ?
        ORDER BY created_at ASC
        """;
    private final DataSource dataSource;

    public JdbcConsultationDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Consultation save(Consultation consultation) {

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(
                INSERT_SQL,
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setLong(1, consultation.getPatientId());
            // statement.setString(2, Consultation.getStatus());
            statement.setString(2, consultation.getStatus().name());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating consultation failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    consultation.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating consultation failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving consultation", e);
        }

        return consultation;
    }

    @Override
    public Consultation update(Consultation consultation) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setLong(1, consultation.getDoctorId());
            statement.setString(2, consultation.getReason());
            statement.setString(3, consultation.getObservations());
            statement.setString(4, consultation.getDiagnosis());
            statement.setString(5, consultation.getPrescribedTreatment());
            statement.setBigDecimal(6, consultation.getCost());
            statement.setLong(7, consultation.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating consultation failed, no rows affected.");
            }
            return consultation;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating consultation", e);
        }
        // return null;
    }

    @Override
    public Optional<Consultation> findById(Long id) {
        try (
                Connection connection
                = dataSource.getConnection(); PreparedStatement statement
                = connection.prepareStatement(
                        FIND_BY_ID_SQL
                )) {
            statement.setLong(1, id);

            try (ResultSet resultSet
                    = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(
                        mapConsultation(resultSet)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erreur pendant la recherche de la consultation.",
                    e
            );
        }
    }

    @Override
    public List<Consultation> findPendingByDate(LocalDate date) {
        try (
                Connection connection
                = dataSource.getConnection(); PreparedStatement statement
                = connection.prepareStatement(
                        FIND_PENDING_BY_DATE_SQL
                )) {
            statement.setString(
                    1,
                    ConsultationStatus.EN_ATTENTE.name()
            );
            statement.setTimestamp(
                    2,
                    Timestamp.valueOf(date.atStartOfDay())
            );
            statement.setTimestamp(
                    3,
                    Timestamp.valueOf(
                            date.plusDays(1).atStartOfDay()
                    )
            );

            try (ResultSet resultSet
                    = statement.executeQuery()) {
                List<Consultation> consultations
                        = new ArrayList<>();

                while (resultSet.next()) {
                    Consultation consultation
                            = new Consultation(
                                    resultSet.getLong("id"),
                                    resultSet.getLong("patient_id"),
                                    ConsultationStatus.valueOf(
                                            resultSet.getString("status")
                                    )
                            );

                    consultations.add(consultation);
                }

                return consultations;
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erreur pendant la recherche des consultations en attente.",
                    e
            );
        }
    }

    private Consultation mapConsultation(
            ResultSet resultSet
    ) throws SQLException {
        Timestamp closedAt
                = resultSet.getTimestamp("closed_at");

        return new Consultation(
                resultSet.getLong("id"),
                resultSet.getLong("patient_id"),
                resultSet.getObject("doctor_id", Long.class),
                resultSet.getString("reason"),
                resultSet.getString("observations"),
                resultSet.getString("diagnosis"),
                resultSet.getString("prescribed_treatment"),
                resultSet.getBigDecimal("cost"),
                ConsultationStatus.valueOf(
                        resultSet.getString("status")
                ),
                closedAt == null
                        ? null
                        : closedAt.toLocalDateTime()
        );
    }

}
