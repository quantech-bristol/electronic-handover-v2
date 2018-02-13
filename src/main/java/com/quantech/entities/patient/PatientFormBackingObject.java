package com.quantech.entities.patient;

import com.quantech.entities.ward.Ward;
import com.quantech.misc.Title;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.ManyToOne;
import java.util.Date;

import static com.quantech.misc.EntityFieldHandler.putNameIntoCorrectForm;

public class PatientFormBackingObject
{
        private Long id;

        private Title title;

        private String firstName;

        private String lastName;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date birthDate;

        private Long nHSNumber;

        private Long hospitalNumber;

        @ManyToOne
        private Ward ward;

        private String bed;

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
            this.firstName = (firstName == null || firstName.equals("") ) ? null : putNameIntoCorrectForm(firstName);
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = (lastName == null || lastName.equals("")) ? null : putNameIntoCorrectForm(lastName);
        }

        public Date getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(Date birthDate) {
            this.birthDate = birthDate;
        }

        public Long getNHSNumber() {
            return nHSNumber;
        }

        public void setNHSNumber (Long NHSNumber) {
            this.nHSNumber = NHSNumber;
        }

        public Long getHospitalNumber() {
            return hospitalNumber;
        }

        public void setHospitalNumber(Long hospitalNumber) {
            this.hospitalNumber = hospitalNumber;
        }

        public Ward getWard() {
            return ward;
        }

        public void setWard(Ward ward) {
            this.ward = ward;
        }

        public String getBed() {
            return bed;
        }

        public void setBed(String bed) {
            this.bed = bed;
        }
}
