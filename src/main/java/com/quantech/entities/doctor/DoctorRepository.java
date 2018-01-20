package com.quantech.entities.doctor;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DoctorRepository extends CrudRepository<Doctor,Long> {

    public List<Doctor> findByLastName(String lastName);

    public List<Doctor> findByFirstName(String firstName);

    public List<Doctor> findByFirstNameAndLastName(String firstName, String lastName);

    public Doctor findByEmail(String email);
}
