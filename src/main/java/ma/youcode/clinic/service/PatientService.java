package ma.youcode.clinic.service;

import ma.youcode.clinic.dao.PatientDAO;
import ma.youcode.clinic.dao.impl.JdbcPatientDAO;
import ma.youcode.clinic.entity.Patient;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PatientService {

    private final PatientDAO patientDAO;

    public PatientService() {
        this.patientDAO = new JdbcPatientDAO();
    }

    public Patient registerPatient(Patient patient) {
        return patientDAO.save(patient);
        
    }

    public List<Patient> getTodayPatients() {
        LocalDate today = LocalDate.now();
        return patientDAO.findAll().stream()
                .filter(p -> p.getArrivedAt() != null && p.getArrivedAt().toLocalDate().equals(today))
                .collect(Collectors.toList());
    }

    public Patient getPatientById(Long id) {
        return patientDAO.findById(id).orElse(null);
    }
}