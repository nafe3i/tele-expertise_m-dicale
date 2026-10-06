package ma.youcode.clinic.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ma.youcode.clinic.entity.Consultation;

public interface ConsultationDAO {

    Consultation save(Consultation consultation);
    Optional<Consultation> findById(Long id);
    List<Consultation> findPendingByDate(LocalDate date);
    Consultation update(Consultation consultation);
    
}
