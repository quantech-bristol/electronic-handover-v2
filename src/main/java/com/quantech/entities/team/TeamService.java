package com.quantech.entities.team;

import com.quantech.entities.doctor.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    @Autowired
    TeamRepository teamRepository;

    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        teamRepository.findAll().forEach(teams::add);
        return teams;
    }

    public Team getTeam(Long id) {
        return teamRepository.findOne(id);
    }

    public void saveTeam(Team team) {
        teamRepository.save(team);
    }

    public void deleteTeam(Long id) {
        teamRepository.delete(id);
    }

    public boolean addDoctor(Doctor doctor, Team team) {
        List<Doctor> doctors = team.getDoctors();
        doctors.add(doctor);
        team.setDoctors(doctors);
        teamRepository.save(team);
        return true;
    }
}
