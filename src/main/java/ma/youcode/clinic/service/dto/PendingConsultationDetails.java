package ma.youcode.clinic.service.dto;

import ma.youcode.clinic.entity.Patient;

public final class PendingConsultationDetails {

    private final Long consultationId;
    private final Patient patient;

    public PendingConsultationDetails(
            Long consultationId,
            Patient patient
    ) {
        this.consultationId = consultationId;
        this.patient = patient;
    }

    public Long getConsultationId() {
        return consultationId;
    }

    public Patient getPatient() {
        return patient;
    }
}
