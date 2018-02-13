package com.quantech.oldEntities.handover;

import com.quantech.oldEntities.doctor.Doctor;
import com.quantech.oldEntities.patient.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HandoverRepository extends CrudRepository<Handover,Long> {

    public List<Handover> findByPatient(Patient p);

    public List<Handover> findByOriginDoctor(Doctor d);

    public List<Handover> findByRecipientDoctor(Doctor d);

    public List<Handover> findByAccepted(Boolean b);
}
