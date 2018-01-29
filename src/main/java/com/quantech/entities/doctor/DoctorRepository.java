package com.quantech.entities.doctor;

import com.quantech.entities.user.UserCore;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DoctorRepository extends CrudRepository<Doctor,Long> {
    public Doctor findById(Long id);

    public List<Doctor> findByUser_LastName(String lastName);

    public List<Doctor> findByUser_FirstName(String firstName);

    public List<Doctor> findByUser_FirstNameAndUser_LastName(String firstName, String lastName);


    public Doctor findByUser_email(String email);

    public void deleteByUser_id(long id);
}
