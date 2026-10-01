package ma.youcode.clinic.dao;

import java.util.List;
import java.util.Optional;

import ma.youcode.clinic.entity.Patient;

public interface PatientDAO {

    Patient save(Patient patient);

    Optional<Patient> findBySocialSecurityNumber(String socialSecurityNumber);

    List<Patient> findAll();
}
