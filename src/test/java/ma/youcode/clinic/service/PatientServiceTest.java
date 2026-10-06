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

public class PatientServiceTest {

    @Test
    public void registersNewPatientAndCreatesPendingConsultation() {
        RecordingPatientDAO patientDAO = new RecordingPatientDAO();
        RecordingConsultationDAO consultationDAO
                = new RecordingConsultationDAO();
        PatientService service
                = new PatientService(patientDAO, consultationDAO);
        Patient patient = validPatient();

        Patient result = service.registerArrival(patient);

        assertSame(patient, result);
        assertEquals(Long.valueOf(42L), result.getId());
        assertEquals(
                result.getId(),
                consultationDAO.savedConsultation.getPatientId()
        );
        assertEquals(
                ConsultationStatus.EN_ATTENTE,
                consultationDAO.savedConsultation.getStatus()
        );
    }

    private Patient validPatient() {
        return new Patient(
                null,
                "Amrani",
                "Sara",
                LocalDate.of(1995, 3, 12),
                "SS-100",
                "12/8",
                75,
                new BigDecimal("37.0"),
                16,
                null
        );
    }

    private static final class RecordingPatientDAO
            implements PatientDAO {

        @Override
        public Optional<Patient> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public Patient save(Patient patient) {
            patient.setId(42L);
            return patient;
        }

        @Override
        public Patient updateVitalSigns(Patient patient) {
            return patient;
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

    private static final class RecordingConsultationDAO
            implements ConsultationDAO {

        private Consultation savedConsultation;

        @Override
        public Consultation save(Consultation consultation) {
            savedConsultation = consultation;
            return consultation;
        }

        @Override
        public Optional<Consultation> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public List<Consultation> findPendingByDate(LocalDate date) {
            return List.of();
        }

        @Override
        public Consultation update(Consultation consultation) {
            return consultation;
        }
    }
}
