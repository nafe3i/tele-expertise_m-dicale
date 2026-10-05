package ma.youcode.clinic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ma.youcode.clinic.dao.ConsultationDAO;
import ma.youcode.clinic.dao.PatientDAO;
import ma.youcode.clinic.entity.Consultation;
import ma.youcode.clinic.entity.Patient;

public class ConsultationService {

    private final ConsultationDAO consultationDAO;
    private final PatientDAO patientDAO;

    public ConsultationService(
            ConsultationDAO consultationDAO,
            PatientDAO patientDAO
    ) {
        this.consultationDAO = consultationDAO;
        this.patientDAO = patientDAO;
    }

    public List<Patient> findPatientTodayEnAttene() {
        List<Consultation> consultations
                = consultationDAO.findPendingByDate(
                        java.time.LocalDate.now()
                );

        List<Patient> patients = new ArrayList<>();

        for (Consultation consultation : consultations) {
            Optional<Patient> patientOptional
                    = patientDAO.findById(consultation.getPatientId());

            patientOptional.ifPresent(patients::add);
        }

        return patients;
    }
}
