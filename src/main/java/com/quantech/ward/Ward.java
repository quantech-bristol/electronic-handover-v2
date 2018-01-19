package com.quantech.ward;

import com.quantech.patient.Patient;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ward {
    // TODO: Decide what fields we want to use.
    // TODO: Decide what exceptions are thrown/conditions are held when using getters and setters.

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
        this.name = "Undefined";
        this.patients = new ArrayList<>();
    }

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
