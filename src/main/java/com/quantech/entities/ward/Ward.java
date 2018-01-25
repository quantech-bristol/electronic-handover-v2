package com.quantech.entities.ward;

import com.quantech.entities.patient.Patient;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ward {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer capacity;

    @NotNull
    private String department;

    @NotNull
    @OneToMany(mappedBy = "ward")
    private List<Patient> patients;

    public Ward() {
        this.capacity = 0;
        this.department = "Undefined";
        this.patients = new ArrayList<>();
    }

    public Ward(String name, Integer capacity, String department) {
        this.name = name;
        this.capacity = capacity;
        this.department = department;
        this.patients = new ArrayList<>();
    }

    public Ward(String name, Integer capacity, String department, List<Patient> patients) {
        this.name = name;
        this.capacity = capacity;
        this.department = department;
        this.patients = patients;
    }

    public Long getId() { return id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
