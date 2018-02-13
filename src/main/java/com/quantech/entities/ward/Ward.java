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

    public Ward() {
        this.name = "Undefined";
    }

    /**
     * ID getter.
     * @return The id associated with a given ward.
     */
    public Long getId() { return id;}

    /**
     * Name getter.
     * @return The name associated with a given ward.
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter.
     * @param name The name to set the name field.
     */
    public void setName(String name) {
        this.name = name;
    }
}
