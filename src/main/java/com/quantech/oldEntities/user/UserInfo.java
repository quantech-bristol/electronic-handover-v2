package com.quantech.oldEntities.user;

import com.quantech.Configurations.SecurityRoles;
import com.quantech.misc.Title;

import java.util.Set;

public interface UserInfo {

    public Title getTitle();
    public void setTitle(Title t);

    public String getFirstName();
    public void setFirstName(String firstName);

    public String getLastName();
    public void setLastName(String lastName);

    public String getEmail();
    public void setEmail(String email);

    public String getUsername();
    public void setUsername(String username);

    public String getPassword();
    public void setPassword(String password);

    public void addAuth(SecurityRoles auth);
    public void removeAuth(SecurityRoles auth) ;
    public boolean hasAuth(SecurityRoles auth) ;

    public boolean getEnabled();
    public void setEnabled(boolean enabled);

    public Long getId();

    public Set<SecurityRoles> getAuthorityStrings();
    public void setAuthorityStrings(Set<SecurityRoles> authorityStrings);
}
