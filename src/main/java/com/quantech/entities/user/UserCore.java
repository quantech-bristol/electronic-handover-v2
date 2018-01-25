package com.quantech.entities.user;

import com.quantech.Configurations.SecurityRoles;
import com.quantech.misc.StringEnumValidation.ValidateString;
import com.quantech.misc.UniqueNameValidation.ValidateStringUnique;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
public class UserCore implements UserDetails{

    @ValidateStringUnique()
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

    @ValidateString(enumClazz = SecurityRoles.class)
    @ElementCollection(fetch = FetchType.EAGER)
    @NotNull
    private List<String> authorityStrings;

    public UserCore(String username, String password, String baseAuth) {

        this.username = username;
        this.password = password;
        authorityStrings = new ArrayList<>();
        authorityStrings.add(baseAuth);
        enabled = true;

    }

    public UserCore() {
        enabled = true;
        authorityStrings = new ArrayList<String>();
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
        for(String auth : authorityStrings)
        {
            authoritiesToReturn.add(new SimpleGrantedAuthority(auth));
        }
        return authoritiesToReturn;
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
        if (!hasAuth(auth)) {
            authorityStrings.add(auth.toString());

        }
    }

    public void removeAuth(SecurityRoles auth) {
        for (int i = 0; i < authorityStrings.size(); i++) {
            if (authorityStrings.get(i).equalsIgnoreCase(auth.toString())) {
                authorityStrings.remove(i); break;
            }
        }
    }

    public boolean hasAuth(SecurityRoles auth) {
        for (int i = 0; i < authorityStrings.size(); i++) {
            if (authorityStrings.get(i).equalsIgnoreCase(auth.toString())){
                return true;
            }
        }
        return false;
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

    public String getBaseAuth()//Used for setting initial authorization... Trying to get this work was a *$%*%@!
    {
        if (authorityStrings.isEmpty()){return "";}
        else return authorityStrings.get(0);
    }
    public void setBaseAuth(String s)//Used for setting initial authorization... Trying to get this work was a *$%*%@!
    {
        authorityStrings.add(s);
    }

    public List<String> getAuthorityStrings() {
        return authorityStrings;
    }

    public void setAuthorityStrings(List<String> authorityStrings) {
        this.authorityStrings = authorityStrings;
    }

}
