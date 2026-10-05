package ma.youcode.clinic.service.dto;

import ma.youcode.clinic.entity.Patient;

public record PendingConsultationDetails(
        Long consultationId,
        Patient patient
        ) {

}
