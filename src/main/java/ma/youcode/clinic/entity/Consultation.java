package ma.youcode.clinic.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Consultation {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private String reason;
    private String observations;
    private String diagnosis;
    private String prescribedTreatment;
    private BigDecimal cost;
    private ConsultationStatus status;
    private LocalDateTime closedAt;

    public Consultation() {
    }

    public Consultation(Long id, Long patientId, Long doctorId, String reason,
                        String observations, String diagnosis, String prescribedTreatment,
                        BigDecimal cost, ConsultationStatus status, LocalDateTime closedAt) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.reason = reason;
        this.observations = observations;
        this.diagnosis = diagnosis;
        this.prescribedTreatment = prescribedTreatment;
        this.cost = cost;
        this.status = status;
        this.closedAt = closedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescribedTreatment() {
        return prescribedTreatment;
    }

    public void setPrescribedTreatment(String prescribedTreatment) {
        this.prescribedTreatment = prescribedTreatment;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public ConsultationStatus getStatus() {
        return status;
    }

    public void setStatus(ConsultationStatus status) {
        this.status = status;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }
}
