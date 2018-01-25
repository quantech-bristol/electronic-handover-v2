package com.quantech;

import com.quantech.Configurations.SecurityRoles;
import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.doctor.DoctorRepository;
import com.quantech.entities.handover.HandoverRepository;
import com.quantech.entities.patient.Patient;
import com.quantech.entities.patient.PatientRepository;
import com.quantech.entities.team.TeamRepository;
import com.quantech.entities.user.UserCore;
import com.quantech.entities.user.UserRepository;
import com.quantech.entities.ward.Ward;
import com.quantech.entities.ward.WardRepository;
import com.quantech.misc.Title;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A set of tools to quickly populate the database for testing purposes.
 */
public class DatabasePopulationTools {
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    HandoverRepository handoverRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WardRepository wardRepository;

    /**
     * Initialises an arbitrary Doctor user.
     * UserCore:    - id: X
     *              - username: "doctorX"
     *              - password: "pass"
     *              - authorities: [Doctor]
     *
     * Doctor:      - id: X
     *              - user_id: X
     *              - title: Dr
     *              - first_name: "Doctor"
     *              - last_name: "ID: X"
     *              - email: "emailX@bristol.ac.uk"
     * @param id The id to be used for the Doctor (and corresponding user), represented by X.
     * @return The UserCore object corresponding to Doctor X.
     */
    public UserCore saveDoctor(Long id) {
        // First creating a user to correspond with Doctor Id:
        List<SecurityRoles> auth = new ArrayList<>();
        auth.add(SecurityRoles.Doctor);
        UserCore u = createUserCore(id,"doctor"+id,auth);

        // Then creating the corresponding Doctor:
        Doctor d = new Doctor(u,Title.Dr,"Doctor","ID: " + id,"email" + id + "@bristol.ac.uk");

        // Saving to the repositories.
        doctorRepository.save(d);
        userRepository.save(u);
        return u;
    }

    /**
     * Initialises an arbitrary Admin user.
     * UserCore:    - id: X
     *              - username: "adminX"
     *              - password: "pass"
     *              - authorities: [Admin]
     * @param id The id to be used by the UserCore.
     * @return The UserCore object representing the Admin user, denoted by X.
     */
    public UserCore saveAdmin(Long id) {
        List<SecurityRoles> auth = new ArrayList<>();
        auth.add(SecurityRoles.Admin);
        UserCore u = createUserCore(id,"admin"+id,auth);

        userRepository.save(u);
        return u;
    }

    // Creation of a user core to add to the repository.
    private UserCore createUserCore(Long id,
                                    String username,
                                    Iterable<SecurityRoles> authorities) {
        UserCore user = new UserCore();
        user.setUsername(username);
        user.setPassword("pass");
        user.setEnabled(true);
        for (SecurityRoles role : authorities)
            user.addAuth(role);
        user.setId(id);
        return user;
    }
}
