package com.quantech.entities.team;

import com.quantech.entities.doctor.Doctor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany
    @NotNull
    private List<Doctor> doctors;

    @NotNull
    private String department;

    public Team() {
        this.doctors = new ArrayList<>();
        this.department = "Undefined";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
