package com.quantech.entities.doctor;

import com.quantech.entities.patient.Patient;
import com.quantech.entities.team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {

    @Autowired
    DoctorRepository doctorRepository;

    // All of these methods should be admin only, which can be restricted in the Controller layer

    /**
     * @return A list of all doctors stored in the doctor repository.
     */
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        doctorRepository.findAll().forEach(doctors::add);
        return doctors;
    }

    /**
     * Finds the doctor object corresponding to a given unique id.
     * @param id The id for which the desired doctor has been assigned.
     * @return The doctor object that corresponds to the id if it exists, null if a doctor with the given id doesn't exist.
     */
    public Doctor getDoctor(Long id) {
        return doctorRepository.findOne(id);
    }

    /**
     * Saves the given doctor object into the repository.
     * @param doctor
     */
    public void saveDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
    }

    /**
     * Deletes the doctor with the corresponding id from the repository.
     * @param id The id corresponding to the doctor to be deleted.
     */
    public void deleteDoctor(Long id) {
        doctorRepository.delete(id);
    }

    /**
     * Adds the given team to the doctor's list.
     * @param team The team for which the doctor should be added to.
     * @param id The id corresponding to the doctor in the repository.
     */
    public void addTeamToDoctor(Team team, Long id) {
        // Add the team to the doctor's list of teams
        //     The doctor needs to be added to the Team's list of doctors over in the Team package
        Doctor doctor = doctorRepository.findOne(id);
        List<Team> teams = doctor.getTeams();
        teams.add(team);
        doctor.setTeams(teams);
        doctorRepository.save(doctor);
    }

    /**
     * Removes the given team from the doctor's list.
     * @param team The team for which the doctor should be removed from.
     * @param id The id corresponding to the doctor in the repository.
     */
    public void removeTeamFromDoctor(Team team, Long id) {
        Doctor doctor = doctorRepository.findOne(id);
        List<Team> teams = doctor.getTeams();
        teams.remove(team);
        doctor.setTeams(teams);
        doctorRepository.save(doctor);
    }

    /**
     * Adds a patient to the doctor's list of patients.
     * @param patient The patient for which the doctor now has responsibility over.
     * @param doctor The doctor that will take responsibility over the patient.
     */
    public void addPatient(Patient patient, Doctor doctor) {
        doctor.addPatient(patient);
        doctorRepository.save(doctor);
    }

    /**

    RE: maintaining a list of patients
        - should we just look at the Teams the Doctor is in, and then call team.getPatients(), rather than
          overlapping ?

     */
}
