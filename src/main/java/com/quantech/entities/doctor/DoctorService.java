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

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        doctorRepository.findAll().forEach(doctors::add);
        return doctors;
    }

    public Doctor getDoctor(Long id) {
        return doctorRepository.findOne(id);
    }

    // save applies to both POST and PUT
    public void saveDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        doctorRepository.delete(id);
    }

    public void addTeamToDoctor(Team team, Long id) {
        // Add the team to the doctor's list of teams
        //     The doctor needs to be added to the Team's list of doctors over in the Team package
        Doctor doctor = doctorRepository.findOne(id);
        List<Team> teams = doctor.getTeams();
        teams.add(team);
        doctor.setTeams(teams);
        doctorRepository.save(doctor);
    }

    public void removeTeamFromDoctor(Team team, Long id) {
        Doctor doctor = doctorRepository.findOne(id);
        List<Team> teams = doctor.getTeams();
        teams.remove(team);
        doctor.setTeams(teams);
        doctorRepository.save(doctor);
    }

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
