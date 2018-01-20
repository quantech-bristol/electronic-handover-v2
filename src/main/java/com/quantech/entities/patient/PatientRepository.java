package com.quantech.entities.patient;

import com.quantech.entities.ward.Ward;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PatientRepository extends CrudRepository<Patient,Long> {

    public List<Patient> findByLastName(String lastName);

    public List<Patient> findByFirstName(String firstName);

    public List<Patient> findByFirstNameAndLastName(String firstName, String lastName);

    public Patient findByNHSNumber(Long num);

    public Patient findByHospitalNumber(Long num);

    public Patient findByWardAndBed(Ward ward, String bed);
}
