package com.quantech.team;

import com.quantech.doctor.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    TeamRepository teamRepository;

    public boolean addDoctor(Doctor doctor, Team team) {
        List<Doctor> doctors = team.getDoctors();
        doctors.add(doctor);
        team.setDoctors(doctors);
        teamRepository.save(team);
        return true;
    }
}
