package com.quantech.entities.patient;

import com.quantech.entities.ward.Ward;
import com.quantech.misc.EntityFieldHandler;
import com.quantech.misc.Title;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.quantech.misc.EntityFieldHandler.putNameIntoCorrectForm;

@Entity
public class Patient {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Title title;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date birthDate;

    // First character needs to be lower case for this field to work, not a typo.
    @Column(unique = true)
    private Long nHSNumber;

    @Column(unique = true)
    private Long hospitalNumber;

    @ManyToOne
    private Ward ward;

    private String bed;

    public Patient() {
    }

    public Patient(PatientFormBackingObject ob) {
        this.title = ob.getTitle();
        this.firstName = putNameIntoCorrectForm(ob.getFirstName());
        this.lastName = putNameIntoCorrectForm(ob.getLastName());
        this.birthDate = ob.getBirthDate();
        this.nHSNumber = ob.getNHSNumber();
        this.hospitalNumber = ob.getHospitalNumber();
        this.ward = ob.getWard();
        this.bed = ob.getBed();
    }

    public Long getId() {
        return id;
    }

    // Should this method actually be here?
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Title getter.
     *
     * @return The title of the patient.
     */
    public Title getTitle() {
        return title;
    }

    /**
     * Title setter
     *
     * @param title the title to set.
     */
    public void setTitle(Title title) {
        this.title = title;
    }

    /**
     * First name getter.
     *
     * @return The patient's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the patient, formatted in the correct way.
     *
     * @param firstName The first name to use.
     */
    public void setFirstName(String firstName) {
        this.firstName = (firstName == null || firstName.equals("")) ? null : putNameIntoCorrectForm(firstName);
    }

    /**
     * Last name getter.
     *
     * @return the patient's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the patient, formatted in the correct way.
     *
     * @param lastName The first name to use.
     * @throws NullPointerException     If the given first name is null
     * @throws IllegalArgumentException If the first name takes the form " *".
     */
    public void setLastName(String lastName) throws NullPointerException, IllegalArgumentException {
        this.lastName = (lastName == null || lastName.equals("")) ? null : putNameIntoCorrectForm(lastName);
    }

    /**
     * Date of birth getter.
     *
     * @return The patient's date of birth.
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Setter for DOB
     *
     * @param birthDate Date of birth.
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    // Use this to check if birth date is valid before setting an attribute with it.
    private Date birthDateValidityChecker(Date birthDate) {
        EntityFieldHandler.nullCheck(birthDate, "date of birth");
        if ((new Date()).before(birthDate))
            throw new IllegalArgumentException("Error: patient's date of birth cannot be in the future.");
        return birthDate;
    }

    /**
     * NHS number getter.
     *
     * @return The patient's NHS number if it is known, null otherwise.
     */
    public Long getNHSNumber() {
        return nHSNumber;
    }

    /**
     * Sets the NHS number of the patient.
     *
     * @param NHSNumber The NHS number to provide the patient.
     */
    public void setNHSNumber(Long NHSNumber) throws NullPointerException, IllegalArgumentException {
        this.nHSNumber = NHSNumber;
    }

    // Used to check that an NHS number is valid before setting an attribute to be equal to it.
    private Long NHSNumberValidityCheck(Long NHSNumber) {
        EntityFieldHandler.nullCheck(NHSNumber, "NHS number");

        int digits = NHSNumber.toString().length();

        if (digits > 10)
            throw new IllegalArgumentException("Error: NHS number has too many digits.");

        // Check that the checksum is correct.
        if (!checksumCorrect(NHSNumber))
            throw new IllegalArgumentException("Error: NHS number is not valid (checksum does not match)");

        return NHSNumber;
    }

    // Checks that the checksum of the NHS number is correct.
    private int checkDigit(Long n) {
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
        int sum = 0;
        int factor = 10;
        for (int i = 0; i < 9; i++) {
            int dig = Character.getNumericValue(digits.charAt(i));
            sum += dig * factor;
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
        int checkSum = Character.getNumericValue(digits.charAt(digits.length() - 1));
        int checkDigit = checkDigit(n);
        return (checkSum == checkDigit && checkDigit != -1);
    }

    /**
     * Returns if the currently set NHS number has valid length.
     *
     * @return true if the currently set NHS number has valid length (10 digits).
     */
    boolean NHSNumberCorrectLength() {
        int digits = this.nHSNumber.toString().length();
        return (digits <= 10);
    }

    /**
     * Checks that the set NHS number is valid, on the basis of the checksum.
     *
     * @return true if the NHS number is valid.
     */
    boolean NHSNumberIsValid() {
        return checksumCorrect(this.nHSNumber);
    }

    /**
     * Hospital number getter.
     *
     * @return The patient's hospital number if it exists, null otherwise.
     */
    public Long getHospitalNumber() {
        return hospitalNumber;
    }

    /**
     * Hospital number setter.
     *
     * @param hospitalNumber The hospital number to set the patient up with.
     */
    public void setHospitalNumber(Long hospitalNumber) {
        this.hospitalNumber = hospitalNumber;
    }

    /**
     * Ward getter.
     *
     * @return The ward that the patient is currently staying in; null if they are not currently in a ward.
     */
    public Ward getWard() {
        return ward;
    }

    /**
     * Ward setter.
     * @param ward The ward to currently assign the patient to.
     */
    public void setWard(Ward ward) {
        this.ward = ward;
    }

    /**
     * Bed getter
     * @return The bed that the patient is currently staying in; null otherwise.
     */
    public String getBed() {
        return bed;
    }

    /**
     * Bed setter.
     * @param bed The bed to assign the patient to.
     */
    public void setBed(String bed) {
        this.bed = bed;
    }
}