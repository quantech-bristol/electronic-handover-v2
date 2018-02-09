package com.quantech.entities.user;

import com.quantech.Configurations.SecurityRoles;
import com.quantech.misc.StringEnumValidation.ValidateEnumValues;
import com.quantech.misc.StringEnumValidation.ValidateListEnumValues;
import com.quantech.misc.Title;
import org.hibernate.validator.constraints.Email;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserFormBackingObject implements UserInfo
{
    @Size(min = 1)
    @NotNull
    private String firstName;

    @Size(min = 1)
    @NotNull
    private String lastName;

    @Email
    @NotNull
    private String email;

    @NotNull
    private Title title;

    @Size(min = 4, max = 20)
    @NotNull
    private String username;

    private Long id;

    private boolean enabled = true;

    //@Size(min = 4, max = 20)
    private String password;

    private Set<SecurityRoles> authorityStrings;

    public UserFormBackingObject(UserInfo core)
    {
        this.username = core.getUsername();
        this.password = core.getPassword();
        this.authorityStrings = core.getAuthorityStrings();
        this.id = core.getId();
        this.email = core.getEmail();
        this.title= core.getTitle();
        this.firstName=core.getFirstName();
        this.lastName = core.getLastName();

    }
    public UserFormBackingObject(String username, String password, SecurityRoles baseAuth) {

        this.username = username;
        this.password = password;
        authorityStrings = new LinkedHashSet<>();
        authorityStrings.add(baseAuth);
        enabled = true;

    }

    public UserFormBackingObject() {
        enabled = true;
        authorityStrings = new LinkedHashSet<>();
    }

    public String getUsername()
    {
        return username;
    }


    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void addAuth(SecurityRoles auth) {
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

    public Set<SecurityRoles> getAuthorityStrings() {
        return authorityStrings;
    }

    public void setAuthorityStrings(Set<SecurityRoles> authorityStrings) {
        this.authorityStrings = authorityStrings;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Title getTitle() {
        return title;
    }

    @Override
    public void setTitle(Title title) {
        this.title = title;
    }


    public UserCore ToUserCore()
    {
        UserCore newUser = new UserCore();
        newUser.updateValues(this);
        return newUser;
    }

}
