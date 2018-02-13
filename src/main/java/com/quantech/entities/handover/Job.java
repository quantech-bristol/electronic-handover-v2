package com.quantech.entities.handover;

import com.quantech.entities.doctor.Doctor;
import com.quantech.misc.Category;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
public class Job {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Type(type = "text")
    private String description;

    @NotNull
    private List<Category> categories;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date creationDate;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date completionDate;

    @NotNull
    @ManyToOne
    private Doctor doctor;

    @NotNull
    private Boolean inTransit;

    public Job() {
        this.inTransit = false;
    }

    /**
     * ID getter.
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * ID setter.
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Description getter.
     * @return Job description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Description setter.
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Creation date getter.
     * @return Job creation date.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Creation date setter.
     * @param creationDate
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Completion date getter.
     * @return Job completion date (null if still ongoing)
     */
    public Date getCompletionDate() {
        return completionDate;
    }

    /**
     * Completion date setter.
     * @param completionDate
     */
    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    /**
     * Doctor getter.
     * @return Doctor currently responsible for the job.
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Doctor setter.
     * @param doctor
     */
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    /**
     * In transit getter.
     * @return True a handover is currently pending, false otherwise.
     */
    public Boolean getInTransit() {
        return inTransit;
    }

    /**
     * In transit setter.
     * @param inTransit
     */
    public void setInTransit(Boolean inTransit) {
        this.inTransit = inTransit;
    }

    /**
     * Categories setter
     * @param categories A list of categories to set the job with.
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * Categories getter.
     * @return The categories that the job is classed under.
     */
    public List<Category> getCategories() {
        return this.categories;
    }
}
