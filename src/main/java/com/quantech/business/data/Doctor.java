package com.quantech.business.data;

import javax.persistence.*;
import javax.print.Doc;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
public class Doctor {
    // TODO: Do error detection/prevention on these getters/setters.
    // TODO: Decide what fields the Doctor entity should have.
    // TODO: Have a rough idea of how the user hierarchy would be made in practice so we don't run into trouble later.
    // May need other ones like "job title/role" and staff number?

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Title title;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Column(unique = true)
    private String email;

    @OneToMany
    @NotNull
    private List<Team> teams;

    @OneToMany(mappedBy = "doctor")
    @NotNull
    private List<Patient> patients;

    @Temporal(TemporalType.DATE)
    private java.util.Calendar lastRenewed;

    @OneToMany(mappedBy = "originDoctor")
    @NotNull
    private List<Handover> sentHandovers;

    @OneToMany(mappedBy = "recipientDoctor")
    @NotNull
    private List<Handover> recievedHandovers;

    public Doctor() {
        this.teams = new ArrayList<>();
        this.patients = new ArrayList<>();
        this.lastRenewed = java.util.Calendar.getInstance();
        this.sentHandovers = new ArrayList<>();
        this.recievedHandovers = new ArrayList<>();
    }

    public void addPatient(Patient patient) {
        this.patients.add(patient);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public Calendar getLastRenewed() {
        return lastRenewed;
    }

    public void setLastRenewed(Calendar lastRenewed) {
        this.lastRenewed = lastRenewed;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}