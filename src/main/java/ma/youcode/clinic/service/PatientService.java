package ma.youcode.clinic.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import ma.youcode.clinic.dao.ConsultationDAO;
import ma.youcode.clinic.dao.PatientDAO;
import ma.youcode.clinic.entity.Consultation;
import ma.youcode.clinic.entity.ConsultationStatus;
import ma.youcode.clinic.entity.Patient;

public class PatientService {

    private final PatientDAO patientDAO;
    private final ConsultationDAO consultationDAO;

    public PatientService(PatientDAO patientDAO, ConsultationDAO consultationDAO) {
        this.patientDAO = patientDAO;
        this.consultationDAO = consultationDAO;

    }

    public Patient registerArrival(Patient patient) {
        validatePatient(patient);

        String socialSecurityNumber
                = patient.getSocialSecurityNumber();

        Optional<Patient> existingPatient
                = patientDAO.findBySocialSecurityNumber(
                        socialSecurityNumber
                );

        Patient registeredPatient;

        if (existingPatient.isEmpty()) {
            registeredPatient = patientDAO.save(patient);
        } else {
            Patient patientToUpdate = existingPatient.get();

            patientToUpdate.setBloodPressure(
                    patient.getBloodPressure()
            );
            patientToUpdate.setHeartRate(
                    patient.getHeartRate()
            );
            patientToUpdate.setTemperature(
                    patient.getTemperature()
            );
            patientToUpdate.setRespiratoryRate(
                    patient.getRespiratoryRate()
            );
            registeredPatient
                    = patientDAO.updateVitalSigns(patientToUpdate);
        }

        Consultation consultation = new Consultation(
                null,
                registeredPatient.getId(),
                ConsultationStatus.EN_ATTENTE
        );
        consultationDAO.save(consultation);

        return registeredPatient;
    }

    public List<Patient> findPatientsArrivedToday() {
        LocalDate today = LocalDate.now();

        return patientDAO.findAll()
                .stream()
                .filter(patient
                        -> patient.getArrivedAt() != null
                )
                .filter(patient
                        -> patient.getArrivedAt()
                        .toLocalDate()
                        .equals(today)
                )
                .sorted(
                        Comparator.comparing(
                                Patient::getArrivedAt
                        )
                )
                .toList();
    }

    private void validatePatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException(
                    "Le patient est obligatoire."
            );
        }

        if (isBlank(patient.getLastName())) {
            throw new IllegalArgumentException(
                    "Le nom du patient est obligatoire."
            );
        }

        if (isBlank(patient.getFirstName())) {
            throw new IllegalArgumentException(
                    "Le prénom du patient est obligatoire."
            );
        }

        if (patient.getBirthDate() == null) {
            throw new IllegalArgumentException(
                    "La date de naissance est obligatoire."
            );
        }

        if (isBlank(patient.getSocialSecurityNumber())) {
            throw new IllegalArgumentException(
                    "Le numéro de sécurité sociale est obligatoire."
            );
        }

        if (isBlank(patient.getBloodPressure())) {
            throw new IllegalArgumentException(
                    "La pression artérielle est obligatoire."
            );
        }

        if (patient.getHeartRate() == null
                || patient.getHeartRate() <= 0) {
            throw new IllegalArgumentException(
                    "La fréquence cardiaque doit être positive."
            );
        }

        if (patient.getTemperature() == null
                || patient.getTemperature()
                        .compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "La température doit être positive."
            );
        }

        if (patient.getRespiratoryRate() == null
                || patient.getRespiratoryRate() <= 0) {
            throw new IllegalArgumentException(
                    "La fréquence respiratoire doit être positive."
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
