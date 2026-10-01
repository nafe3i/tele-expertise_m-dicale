package ma.youcode.clinic.dao;

import java.time.LocalDate;
import java.util.List;

import ma.youcode.clinic.entity.Consultation;

public interface ConsultationDAO {

    Consultation save(Consultation consultation);

    List<Long> findCompletedPatientIdsByDate(LocalDate date);
}
