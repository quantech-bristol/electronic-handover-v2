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

    /**
     * Finds a list of all teams stored in the system.
     * @return A list of all teams stored in the repository.
     */
    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        teamRepository.findAll().forEach(teams::add);
        return teams;
    }

    /**
     * Finds the team corresponding to the unique id.
     * @param id The id that corresponds to a team in the repository.
     * @return The team that corresponds to the id if it exists, false otherwise.
     */
    public Team getTeam(Long id) {
        return teamRepository.findOne(id);
    }

    /**
     * Saves the given team into the repository.
     * @param team The team that is to be saved.
     */
    public void saveTeam(Team team) {
        teamRepository.save(team);
    }

    /**
     * Deletes a given team from the repository.
     * @param id The id corresponding to a team in the repository.
     */
    public void deleteTeam(Long id) {
        teamRepository.delete(id);
    }

    // I think I spotted a potential bug in this one; could cause multiple teams in the same repository.
    // A fix for this is to use a team id as input, not a team object.
    public boolean addDoctor(Doctor doctor, Team team) {
        List<Doctor> doctors = team.getDoctors();
        doctors.add(doctor);
        team.setDoctors(doctors);
        teamRepository.save(team);
        return true;
    }
}
