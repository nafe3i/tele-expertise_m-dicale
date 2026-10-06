package ma.youcode.clinic.service;

import ma.youcode.clinic.dao.ConsultationDAO;
import ma.youcode.clinic.dao.PatientDAO;
import ma.youcode.clinic.entity.Consultation;
import ma.youcode.clinic.entity.ConsultationStatus;
import ma.youcode.clinic.entity.Patient;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ConsultationServiceTest {

    @Test
    public void closesPendingConsultationWithDoctorAndFixedCost() {
        Consultation consultation = new Consultation(
                7L,
                42L,
                ConsultationStatus.EN_ATTENTE
        );
        RecordingConsultationDAO consultationDAO
                = new RecordingConsultationDAO(consultation);
        ConsultationService service = new ConsultationService(
                consultationDAO,
                new UnusedPatientDAO()
        );

        Consultation result = service.closeConsultation(
                7L,
                3L,
                "Fièvre",
                "Fatigue",
                "Infection virale",
                "Repos"
        );

        assertSame(consultation, result);
        assertSame(consultation, consultationDAO.updatedConsultation);
        assertEquals(Long.valueOf(3L), result.getDoctorId());
        assertEquals(
                ConsultationStatus.TERMINEE,
                result.getStatus()
        );
        assertEquals(new BigDecimal("150.00"), result.getCost());
    }

    private static final class RecordingConsultationDAO
            implements ConsultationDAO {

        private final Consultation consultation;
        private Consultation updatedConsultation;

        private RecordingConsultationDAO(Consultation consultation) {
            this.consultation = consultation;
        }

        @Override
        public Consultation save(Consultation consultation) {
            return consultation;
        }

        @Override
        public Optional<Consultation> findById(Long id) {
            return Optional.of(consultation);
        }

        @Override
        public List<Consultation> findPendingByDate(LocalDate date) {
            return List.of();
        }

        @Override
        public Consultation update(Consultation consultation) {
            updatedConsultation = consultation;
            return consultation;
        }
    }

    private static final class UnusedPatientDAO implements PatientDAO {

        @Override
        public Optional<Patient> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public Patient save(Patient patient) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Patient updateVitalSigns(Patient patient) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<Patient> findBySocialSecurityNumber(
                String socialSecurityNumber
        ) {
            return Optional.empty();
        }

        @Override
        public List<Patient> findAll() {
            return List.of();
        }
    }
}
