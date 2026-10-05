package ma.youcode.clinic.service;

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
        if (patient == null) {
            throw new IllegalArgumentException(
                    "Le patient est obligatoire."
            );
        }

        String socialSecurityNumber
                = patient.getSocialSecurityNumber();

        if (socialSecurityNumber == null
                || socialSecurityNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Le numéro de sécurité sociale est obligatoire."
            );
        }

        Optional<Patient> existingPatient
                = patientDAO.findBySocialSecurityNumber(
                        socialSecurityNumber
                );

        Patient registeredPatient;

        if (existingPatient.isEmpty()) {
            // patient.setArrivedAt(LocalDateTime.now());
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
            // patientToUpdate.setArrivedAt(
            //         LocalDateTime.now()
            // );

            registeredPatient
                    = patientDAO.updateVitalSigns(patientToUpdate);
        }
        Consultation consultation = new Consultation();
        consultation.setPatientId(registeredPatient.getId());
        consultation.setStatus(ConsultationStatus.EN_ATTENTE);

        consultationDAO.save(consultation);

        
        return registeredPatient;
    }

}
