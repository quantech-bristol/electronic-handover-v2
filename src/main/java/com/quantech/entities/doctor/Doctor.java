package com.quantech.entities.doctor;

import com.quantech.entities.team.Team;
import com.quantech.entities.user.UserCore;
import com.quantech.misc.Title;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Doctor {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) // Matches ID with UserCore
    @MapsId
    private UserCore user;

    @OneToMany
    @NotNull
    private List<Team> teams;

    public Doctor() {
        this.teams = new ArrayList<>();
    }

    public Doctor(UserCore user) {
        this.user = user;
        this.teams = new ArrayList<>();
    }

    /**
     * ID getter.
     * @return The doctor's, i.e. corresponding UserCore's, ID.
     */
    public Long getId() {
        return user.getId();
    }

    /**
     * Title getter.
     * @return The doctor's title.
     */
    public Title getTitle() {
        return user.getTitle();
    }

    /**
     * Title setter.
     * @param title The doctor's title.
     */
    public void setTitle(Title title) {
       user.setTitle(title);
    }

    /**
     * First name getter.
     * @return The doctor's first name.
     */
    public String getFirstName() {
        return user.getFirstName();
    }

    /**
     * First name setter.
     * @param firstName The doctor's first name.
     */
    public void setFirstName(String firstName) {
        user.setFirstName(firstName);;
    }

    /**
     * Last name getter.
     * @return The doctor's last name.
     */
    public String getLastName() {
       return user.getLastName();
    }

    /**
     * Last name setter.
     * @param lastName The doctor's last name.
     */
    public void setLastName(String lastName) {
        user.setLastName(lastName);
    }

    /**
     * Email getter.
     * @return The doctor's email.
     */
    public String getEmail() {
       return user.getEmail();
    }

    /**
     * Email setter.
     * @param email The doctor's email.
     */
    public void setEmail(String email) {
        user.setEmail(email);
    }

    /**
     * Teams getter.
     * @return The doctor's teams.
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Teams setter.
     * @param teams The new list of teams.
     */
    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    /**
     * UserCore getter.
     * @return The doctor's corresponding UserCore object.
     */
    public UserCore getUser()
    {
        return user;
    }

    /**
     * Set the user that corresponds to the doctor object.
     * @param user The user to set
     */
    public void setUser(UserCore user)
    {
        this.user = user;
    }
}
