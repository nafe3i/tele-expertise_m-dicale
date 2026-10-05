package ma.youcode.clinic.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class Patient {

    private Long id;
    private String lastName;
    private String firstName;
    private LocalDate birthDate;
    private String socialSecurityNumber;
    private String bloodPressure;
    private Integer heartRate;
    private BigDecimal temperature;
    private Integer respiratoryRate;
    private LocalDateTime arrivedAt;

    public Patient() {
    }

    public Patient(Long id, String lastName, String firstName, LocalDate birthDate,
                   String socialSecurityNumber, String bloodPressure, Integer heartRate,
                   BigDecimal temperature, Integer respiratoryRate, LocalDateTime arrivedAt) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.socialSecurityNumber = socialSecurityNumber;
        this.bloodPressure = bloodPressure;
        this.heartRate = heartRate;
        this.temperature = temperature;
        this.respiratoryRate = respiratoryRate;
        this.arrivedAt = arrivedAt;
    }
//     public Patient(String bloodPressure,Integer heartRate,BigDecimal temperature,Integer respiratoryRate,LocalDateTime arrivedAt){
//         this.bloodPressure = bloodPressure;
//         this.heartRate = heartRate;
//         this.temperature = temperature;
//         this.respiratoryRate = respiratoryRate;
//         this.arrivedAt = arrivedAt;
// }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public Integer getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(Integer respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public LocalDateTime getArrivedAt() {
        return arrivedAt;
    }

    public void setArrivedAt(LocalDateTime arrivedAt) {
        this.arrivedAt = arrivedAt;
    }
}
