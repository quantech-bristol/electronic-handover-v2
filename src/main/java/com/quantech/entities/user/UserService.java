package com.quantech.entities.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    public UserService() {

    }

    public void delUser(String user) {
        userRepository.deleteUserCoreByUsername(user);
    }

    public void editPassword(String user, String newPass)
    {
        UserCore userToEdit = userRepository.findUserCoreByUsername(user);
        userToEdit.setPassword(newPass);
        userRepository.save(userToEdit);

    }
    //TODO add sanity checks
    public boolean saveUser(UserCore user) {
        userRepository.save(user);
        return true;
    }

    public UserCore findUserById(long id) {
        UserCore newUser = userRepository.getUserCoreByIdEquals(id);
        return newUser;
    }

    public UserCore findUserByUsername(String username) {
        UserCore newUser = userRepository.findUserCoreByUsername(username);
        return newUser;
    }
    //Enables userService to authenticate its users for the Security config.
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (s.matches("quantech")) {
            return rootUser();
        }
        UserCore newUser = userRepository.findUserCoreByUsername(s);
        if (newUser==null) {
            throw new UsernameNotFoundException(s);
        }
        List<GrantedAuthority> authorities  = new ArrayList<GrantedAuthority>();
        for(String auth : newUser.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(auth));
        }
        return new User(newUser.getUsername(), newUser.getPassword(), authorities);
    }

    private UserDetails rootUser() {
        List<GrantedAuthority> authorities  = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("Admin"));
        return new User("quantech", "quantech", authorities);
    }
}
