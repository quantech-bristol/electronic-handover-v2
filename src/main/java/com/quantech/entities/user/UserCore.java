package com.quantech.entities.user;

import com.quantech.Configurations.SecurityRoles;
import com.quantech.misc.EntityFieldHandler;
import com.quantech.misc.StringEnumValidation.ValidateEnumValues;
import com.quantech.misc.StringEnumValidation.ValidateListEnumValues;
import com.quantech.misc.Title;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
public class UserCore implements UserDetails, UserInfo{
    @Size(min = 1)
    @NotNull
    private String firstName;

    @Size(min = 1)
    @NotNull
    private String lastName;

    @Column(unique = true)
    @Email
    @NotNull
    private String email;

    //@ValidateEnumValues(enumClazz = Title.class)
    @NotNull
    private Title title;

    @Size(min = 4, max = 20)
    @Column(unique = true, nullable = false)
    @NotNull
    private String username;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @NotNull
    private boolean enabled = true;

    @Size(min = 4, max = 20)
    @Column
    @NotNull
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @NotNull
    private Set<SecurityRoles> authorityStrings;

    public UserCore(String username, String password, SecurityRoles baseAuth, Title title, String firstName, String surname, String email) {

        this.username = (String) EntityFieldHandler.nullCheck(username,"username");
        this.password = (String) EntityFieldHandler.nullCheck(password,"password");
        authorityStrings = new LinkedHashSet<>();
        authorityStrings.add((SecurityRoles) EntityFieldHandler.nullCheck(baseAuth, "authorisation"));
        enabled = true;
        this.title = (Title) EntityFieldHandler.nullCheck(title,"title");
        this.firstName = EntityFieldHandler.putNameIntoCorrectForm( EntityFieldHandler.nameValidityCheck(firstName) );
        this.lastName = EntityFieldHandler.putNameIntoCorrectForm( EntityFieldHandler.nameValidityCheck(surname) );
        this.email = EntityFieldHandler.emailValidityCheck(email);

    }

    public UserCore() {
        enabled = true;
        authorityStrings = new LinkedHashSet<>();
    }

    public String getUsername()
    {
        return username;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authoritiesToReturn  = new ArrayList<GrantedAuthority>();
        for(SecurityRoles auth : authorityStrings)
        {
            authoritiesToReturn.add(new SimpleGrantedAuthority(auth.toString()));
        }
        return authoritiesToReturn;
    }


    public void setUsername(String username)
    {
        EntityFieldHandler.nullCheck(username,"username");
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        EntityFieldHandler.nullCheck(password,"password");
        int length = password.length();
        if (length < 4 || length > 20)
            throw new IllegalArgumentException("Error: password needs to be between 4 and 20 characters long.");
        this.password = password;
    }

    public void addAuth(SecurityRoles auth) {
        EntityFieldHandler.nullCheck(auth,"authorisation");
        authorityStrings.add(auth);
    }

    public void removeAuth(SecurityRoles auth) {
       authorityStrings.remove(auth);
    }

    public boolean hasAuth(SecurityRoles auth) {
     return authorityStrings.contains(auth);
    }

    public boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<SecurityRoles> getAuthorityStrings() {
        return authorityStrings;
    }

    public void setAuthorityStrings(Set<SecurityRoles> authorityStrings) {
        EntityFieldHandler.nullCheck(authorityStrings,"authority strings");
        this.authorityStrings = authorityStrings;
    }

    public void updateValues(UserInfo user) {
        EntityFieldHandler.nullCheck(user,"user info");
        this.authorityStrings = user.getAuthorityStrings();
        this.username = user.getUsername();
        if (user.getPassword().length() != 0) {
            this.password = user.getPassword();
        }
        this.title= user.getTitle();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }


    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        EntityFieldHandler.nameValidityCheck(firstName);
        this.firstName = EntityFieldHandler.putNameIntoCorrectForm(firstName);
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        EntityFieldHandler.nameValidityCheck(lastName);
        this.lastName = EntityFieldHandler.putNameIntoCorrectForm(lastName);
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = EntityFieldHandler.emailValidityCheck(email);
    }

    @Override
    public Title getTitle() {
        return title;
    }

    @Override
    public void setTitle(Title title) {
        EntityFieldHandler.nullCheck(title,"title");
        this.title = title;
    }

    public String getFullName()
    {
        return title.toString() + " " + firstName + " " + lastName;
    }



}
