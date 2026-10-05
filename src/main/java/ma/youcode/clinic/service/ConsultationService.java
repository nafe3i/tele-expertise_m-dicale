package ma.youcode.clinic.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ma.youcode.clinic.dao.ConsultationDAO;
import ma.youcode.clinic.dao.PatientDAO;
import ma.youcode.clinic.entity.Consultation;
import ma.youcode.clinic.entity.ConsultationStatus;
import ma.youcode.clinic.entity.Patient;
import ma.youcode.clinic.service.dto.PendingConsultationDetails;

public class ConsultationService {

    private final ConsultationDAO consultationDAO;
    private final PatientDAO patientDAO;

    private static final BigDecimal CONSULTATION_COST
            = new BigDecimal("150.00");

    public ConsultationService(
            ConsultationDAO consultationDAO,
            PatientDAO patientDAO
    ) {
        this.consultationDAO = consultationDAO;
        this.patientDAO = patientDAO;
    }

    public List<PendingConsultationDetails> findPendingToday() {
        List<Consultation> consultations
                = consultationDAO.findPendingByDate(
                        LocalDate.now()
                );

        List<PendingConsultationDetails> details
                = new ArrayList<>();

        for (Consultation consultation : consultations) {
            Patient patient = patientDAO
                    .findById(consultation.getPatientId())
                    .orElseThrow(()
                            -> new IllegalStateException(
                            "Patient introuvable pour la consultation "
                            + consultation.getId()
                    )
                    );

            details.add(
                    new PendingConsultationDetails(
                            consultation.getId(),
                            patient
                    )
            );
        }

        return details;
    }

    public Consultation closeConsultation(
            Long consultationId,
            Long doctorId,
            String reason,
            String observations,
            String diagnosis,
            String prescribedTreatment
    ) {
        validateClosingData(
                consultationId,
                doctorId,
                reason,
                observations,
                diagnosis,
                prescribedTreatment
        );

        Consultation consultation = consultationDAO
                .findById(consultationId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Consultation introuvable."
                )
                );

        if (consultation.getStatus()
                != ConsultationStatus.EN_ATTENTE) {
            throw new IllegalStateException(
                    "Cette consultation est déjà terminée."
            );
        }

        consultation.setDoctorId(doctorId);
        consultation.setReason(reason.trim());
        consultation.setObservations(observations.trim());
        consultation.setDiagnosis(diagnosis.trim());
        consultation.setPrescribedTreatment(
                prescribedTreatment.trim()
        );
        consultation.setCost(CONSULTATION_COST);
        consultation.setStatus(
                ConsultationStatus.TERMINEE
        );

        return consultationDAO.update(consultation);
    }

    private void validateClosingData(
            Long consultationId,
            Long doctorId,
            String reason,
            String observations,
            String diagnosis,
            String prescribedTreatment
    ) {
        if (consultationId == null || consultationId <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant de la consultation est invalide."
            );
        }

        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant du médecin est invalide."
            );
        }

        if (isBlank(reason)) {
            throw new IllegalArgumentException(
                    "Le motif est obligatoire."
            );
        }

        if (isBlank(observations)) {
            throw new IllegalArgumentException(
                    "Les observations sont obligatoires."
            );
        }

        if (isBlank(diagnosis)) {
            throw new IllegalArgumentException(
                    "Le diagnostic est obligatoire."
            );
        }

        if (isBlank(prescribedTreatment)) {
            throw new IllegalArgumentException(
                    "Le traitement prescrit est obligatoire."
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
