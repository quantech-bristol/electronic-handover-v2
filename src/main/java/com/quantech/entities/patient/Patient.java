package com.quantech.entities.patient;

import com.quantech.entities.doctor.Doctor;
import com.quantech.misc.EntityFieldHandler;
import com.quantech.misc.Title;
import com.quantech.entities.ward.Ward;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.quantech.misc.EntityFieldHandler.nullCheck;
import static com.quantech.misc.EntityFieldHandler.putNameIntoCorrectForm;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date dateOfAdmission;

    @NotNull
    @Type(type="text")
    private String relevantHistory;

    @NotNull
    @Type(type="text")
    private String socialIssues;

    // TODO: Maybe change this to be a List of enums depending on the team the doctor belongs to.
    @NotNull
    @Type(type="text")
    private String risks;

    @NotNull
    @Type(type="text")
    private String recommendations;

    @NotNull
    @Type(type="text")
    private String diagnosis;

    @NotNull
    private Boolean discharged;

    public Patient() {
        this.discharged = false;
        this.dateOfAdmission = new Date();
    }

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
                   String diagnosis,
                   Boolean discharged) {
        this.doctor = (Doctor) nullCheck(doctor,"doctor");
        this.title = (Title) nullCheck(title,"title");;
        this.firstName = putNameIntoCorrectForm( (String) nullCheck(firstName,"first name") );
        this.lastName = putNameIntoCorrectForm( (String) nullCheck(lastName,"last name") );
        this.birthDate = birthDateValidityChecker(birthDate);
        this.nHSNumber = NHSNumberValidityCheck(nHSNumber);
        this.hospitalNumber = (Long) nullCheck(hospitalNumber,"hospital number");
        this.ward = (Ward) nullCheck(ward,"ward");
        this.bed = (String) nullCheck(bed,"bed");
        this.dateOfAdmission =  dateOfAdmissionValidityCheck(dateOfAdmission);
        this.relevantHistory = (String) nullCheck(relevantHistory,"relevant history");
        this.socialIssues = (String) nullCheck(socialIssues,"social issues");
        this.risks = (String) nullCheck(risks,"risks");
        this.recommendations = (String) nullCheck(recommendations,"recommendations");
        this.diagnosis = (String) nullCheck(diagnosis,"diagnosis");
        this.discharged = discharged;
    }

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
        this.doctor = doctor;
    }
    public Title getTitle() {
        return title;
    }

    /**
     * Title setter
     * @param title the title to set.
     */
    public void setTitle(Title title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the patient, formatted in the correct way.
     * @param firstName The first name to use.
     * @throws NullPointerException If the given first name is null
     * @throws IllegalArgumentException If the first name takes the form " *".
     */
    public void setFirstName(String firstName) throws NullPointerException, IllegalArgumentException {
        this.firstName = (firstName == null || firstName.equals("") ) ? null : putNameIntoCorrectForm(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the patient, formatted in the correct way.
     * @param lastName The first name to use.
     * @throws NullPointerException If the given first name is null
     * @throws IllegalArgumentException If the first name takes the form " *".
     */
    public void setLastName(String lastName) throws NullPointerException, IllegalArgumentException {
        this.lastName = (lastName == null || lastName.equals("")) ? null : putNameIntoCorrectForm(lastName);
    }

    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Setter for DOB
     * @param birthDate Date of birth.
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    // Use this to check if birth date is valid before setting an attribute with it.
    private Date birthDateValidityChecker(Date birthDate) {
        EntityFieldHandler.nullCheck(birthDate,"date of birth");
        if (this.dateOfAdmission != null && dateOfAdmission.before(birthDate))
            throw new IllegalArgumentException("Error: patient's date of admission cannot be before birth date");
        return birthDate;
    }

    public Long getNHSNumber() {
        return nHSNumber;
    }

    /**
     * Sets the NHS number of the patient.
     * @param NHSNumber The NHS number to provide the patient.
     */
    public void setNHSNumber (Long NHSNumber) throws NullPointerException, IllegalArgumentException {
        this.nHSNumber = NHSNumber;
    }

    // Used to check that an NHS number is valid before setting an attribute to be equal to it.
    private Long NHSNumberValidityCheck(Long NHSNumber) {
        EntityFieldHandler.nullCheck(NHSNumber,"NHS number");

        int digits = NHSNumber.toString().length();

        if (digits > 10)
            throw new IllegalArgumentException("Error: NHS number has too many digits.");

        // Check that the checksum is correct.
        if ( !checksumCorrect(NHSNumber) )
            throw new IllegalArgumentException("Error: NHS number is not valid (checksum does not match)");

        return NHSNumber;
    }

    // Checks that the checksum of the NHS number is correct.
    public int checkDigit(Long n) {
        String digits = n.toString();

        if (digits.length() < 10) {
            StringBuilder zeros = new StringBuilder();
            for (int i = 0; i < 10 - digits.length(); i++) {
                zeros.append('0');
            }
            digits = zeros.toString() + digits;
        }

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
        if (checkDigit == 10)
            return -1;
        if (checkDigit == 11)
            checkDigit = 0;
        // 5- Check the remainder matches the check digit.
        return checkDigit;
    }

    // Checks if the check digit generated from an NHS number matches the checksum provided in the number.
    private boolean checksumCorrect(Long n) {
        String digits = n.toString();
        int checkSum = Character.getNumericValue(digits.charAt(digits.length()-1));
        int checkDigit = checkDigit(n);
        return (checkSum == checkDigit && checkDigit != -1);
    }

    /**
     * Returns if the currently set NHS number has valid length.
     * @return true if the currently set NHS number has valid length (10 digits).
     */
    boolean NHSNumberCorrectLength() {
        int digits = this.nHSNumber.toString().length();
        return (digits <= 10);
    }

    /**
     * Checks that the set NHS number is valid, on the basis of the checksum.
     * @return true if the NHS number is valid.
     */
    boolean NHSNumberIsValid() {
        return checksumCorrect(this.nHSNumber);
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
        this.dateOfAdmission = dateOfAdmissionValidityCheck(dateOfAdmission);
    }

    private Date dateOfAdmissionValidityCheck(Date dateOfAdmission) {
        if (birthDate != null && birthDate.after(dateOfAdmission))
            throw new IllegalArgumentException("Error: date of admission cannot be after patient's date of birth.");
        return dateOfAdmission;
    }

    public String getRelevantHistory() {
        return relevantHistory;
    }

    public void setRelevantHistory(String relevantHistory) {
        this.relevantHistory = relevantHistory;
    }

    public String getSocialIssues() {
        return socialIssues;
    }

    public void setSocialIssues(String socialIssues) {
        this.socialIssues = socialIssues;
    }

    public String getRisks() {
        return risks;
    }

    public void setRisks(String risks) {
        this.risks = risks;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Boolean getDischarged() {
        return discharged;
    }

    public void setDischarged(Boolean discharged) {
        this.discharged = discharged;
    }
}
