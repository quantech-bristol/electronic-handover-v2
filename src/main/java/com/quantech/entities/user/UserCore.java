package com.quantech.entities.user;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserCore
{

    @Column(unique = true, nullable = false)
    @NotNull
    private String username;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @NotNull
    private boolean enabled;

    @Column
    @NotNull
    private String password;

    public List<String> getAuthorities()
    {
        return authorities;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @NotNull
    private List<String> authorities;

    public UserCore(String username, String password, String baseAuth)
    {
        this.username = username;
        this.password = password;
        authorities = new ArrayList<>();
        authorities.add(baseAuth);

    }

    public UserCore()
    {
        enabled = true;
        authorities = new ArrayList<String>();
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

    public void addAuth(String auth)
    {
        if (!hasAuth(auth))
        {
            authorities.add(auth);
        }
    }

    public void removeAuth(String auth)
    {
        for (int i = 0; i < authorities.size(); i++)
        {
            if (authorities.get(i).equalsIgnoreCase(auth)){
                authorities.remove(i); break;}
        }
    }
    public boolean hasAuth(String auth)
    {
        for (int i = 0; i < authorities.size(); i++)
        {
            if (authorities.get(i).equalsIgnoreCase(auth)){return true;}
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

    public void setId(Long id)
    {
        this.id = id;
    }
}
