package com.quantech.entities.patient;

import com.quantech.entities.doctor.Doctor;
import com.quantech.entities.doctor.DoctorService;
import com.quantech.entities.user.UserCore;
import com.quantech.misc.Title;
import com.quantech.entities.ward.Ward;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Patient {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Doctor doctor;

    @NotNull
    private Title title;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    // TODO: sort date format as it's currently going in wrong
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date birthDate;

    // First character needs to be lower case for this field to work, not a typo.
    @NotNull
    @Column(unique = true)
    private Long nHSNumber;

    @NotNull
    @Column(unique = true)
    private Long hospitalNumber;

    @NotNull
    @ManyToOne
    private Ward ward;

    @NotNull
    private String bed;

    // TODO: sort date format as it's currently inputting wrong
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dateOfAdmission;

    @NotNull
    private String relevantHistory;

    @NotNull
    private String socialIssues;

    // TODO: Maybe change this to be a List of enums depending on the team the doctor belongs to.
    @NotNull
    private String risks;

    @NotNull
    private String recommendations;

    @NotNull
    private String diagnosis;

    // Empty constructor.
    public Patient() {
    }

    // Constructor to create new patient based on existing values.
    // TODO: Error checking.
    public Patient(Doctor doctor,
                   Title title,
                   String firstName,
                   String lastName,
                   Date birthDate,
                   Long nHSNumber,
                   Long hospitalNumber,
                   Ward ward,
                   String bed,
                   Date dateOfAdmission,
                   String relevantHistory,
                   String socialIssues,
                   String risks,
                   String recommendations,
                   String diagnosis) {
        this.doctor = doctor;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.nHSNumber = nHSNumber;
        this.hospitalNumber = hospitalNumber;
        this.ward = ward;
        this.bed = bed;
        this.dateOfAdmission = dateOfAdmission;
        this.relevantHistory = relevantHistory;
        this.socialIssues = socialIssues;
        this.risks = risks;
        this.recommendations = recommendations;
        this.diagnosis = diagnosis;
    }

    // TODO: Carry out error detection/prevention on these getters and setters.
    public Long getId() {
        return id;
    }

    // Should this method actually be here?
    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        nullCheck(doctor,"doctor");
        this.doctor = doctor;
    }
    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        nullCheck(title,"title");
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        nullCheck(firstName, "first name");
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        nullCheck(lastName,"last name");
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Setter for DOB
     * @param birthDate Date of birth.
     * @throws NullPointerException if null.
     * @throws IllegalArgumentException if birth date after patient's date of admission.
     */
    public void setBirthDate(Date birthDate) {
        nullCheck(birthDate,"date of birth");
        if (this.dateOfAdmission != null && dateOfAdmission.before(birthDate))
            throw new IllegalArgumentException("Error: patient's date of admission cannot be before birth date");
        this.birthDate = birthDate;
    }

    public Long getNHSNumber() {
        return nHSNumber;
    }

    /**
     * Sets the NHS number of the patient.
     * @param NHSNumber The NHS number to provide the patient.
     * @throws NullPointerException If the NHS number provided is null.
     * @throws IllegalArgumentException If either the number doesn't have 10 digits, or the checksum doesn't match.
     */
    public void setNHSNumber (Long NHSNumber) throws NullPointerException, IllegalArgumentException {
        nullCheck(NHSNumber,"NHS number");

        // TODO: complete this; have not uncommented yet because changes to validity checks may cause errors in existing databases.
        int digits = NHSNumber.toString().length();

        if (digits < 10)
            throw new NullPointerException("Error: NHS number has too few digits.");
        if (digits > 10)
            throw new NullPointerException("Error: NHS number has too many digits.");

        // Check that the checksum is correct.
        if ( checksumCorrect(NHSNumber) )
            throw new NullPointerException("Error: NHS number is not valid (checksum does not match)");


        this.nHSNumber = NHSNumber;
    }

    // Checks that the checksum of the NHS number is correct.
    public int checkDigit(Long n) {
        String digits = n.toString();

        // Applying Modulus 11 algorithm:
        // Source: http://www.datadictionary.nhs.uk/data_dictionary/attributes/n/nhs/nhs_number_de.asp?shownav=1
        // 1- Apply factors:
        int sum = 0; int factor = 10;
        for (int i = 0; i < 9; i++) {
            int dig = Character.getNumericValue(digits.charAt(i));
            sum += dig*factor;
            factor--;
        }
        // 2- Find the remainder of dividing by 11.
        int r = sum % 11;
        // 3- Take the remainder away from 11 to get the check digit.
        int checkDigit = 11 - r;
        // 4- If the value is 10 then the check digit used is 0. If it is 0, then the number is invalid.
        if (checkDigit == 0)
            return -1;
        if (checkDigit == 10)
            checkDigit = 0;
        // 5- Check the remainder matches the check digit.
        return checkDigit;
    }

    private boolean checksumCorrect(Long n) {
        String digits = n.toString();
        int checkSum = Character.getNumericValue(digits.charAt(digits.length()-1));
        int checkDigit = checkDigit(n);
        return (checkSum == checkDigit && checkDigit != -1);
    }

    public Long getHospitalNumber() {
        return hospitalNumber;
    }

    public void setHospitalNumber(Long hospitalNumber) {
        nullCheck(hospitalNumber,"hospital number");
        this.hospitalNumber = hospitalNumber;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        nullCheck(ward,"ward");
        this.ward = ward;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        nullCheck(bed,"bed");
        this.bed = bed;
    }

    public Date getDateOfAdmission() {
        return dateOfAdmission;
    }

    /**
     * Setter for DOA.
     * @param dateOfAdmission The patient's date of admission.
     * @throws NullPointerException if the value is null.
     * @throws IllegalArgumentException if the date of admission is before the patient's date of birth.
     */
    public void setDateOfAdmission(Date dateOfAdmission) throws NullPointerException, IllegalArgumentException {
        nullCheck(dateOfAdmission, "date of admission");
        if (birthDate != null && birthDate.after(dateOfAdmission))
            throw new IllegalArgumentException("Error: date of admission cannot be after patient's date of birth.");
        this.dateOfAdmission = dateOfAdmission;
    }

    public String getRelevantHistory() {
        return relevantHistory;
    }

    public void setRelevantHistory(String relevantHistory) {
        nullCheck(relevantHistory,"relevantHistory");
        this.relevantHistory = relevantHistory;
    }

    public String getSocialIssues() {
        return socialIssues;
    }

    public void setSocialIssues(String socialIssues) {
        nullCheck(socialIssues,"socialIssues");
        this.socialIssues = socialIssues;
    }

    public String getRisks() {
        return risks;
    }

    public void setRisks(String risks) {
        nullCheck(risks,"risks");
        this.risks = risks;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        nullCheck(recommendations,"recommendations");
        this.recommendations = recommendations;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        nullCheck(diagnosis,"diagnosis");
        this.diagnosis = diagnosis;
    }

    // Throws NullPointerException with custom error message.
    private void nullCheck(Object obj, String name) throws NullPointerException {
        if (obj == null)
            throw new NullPointerException("Error: " + name + " in patient cannot be assigned null value.");

    }
}
