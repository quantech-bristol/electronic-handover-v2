package com.quantech.entities.user;

import com.quantech.Configurations.SecurityRoles;
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
        if (user.getUsername() != "quantech") {
            userRepository.save(user);
            return false;
        }
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
        return newUser;
    }

    private UserCore rootUser() {
        UserCore rootUser = new UserCore("quantech","quantech", SecurityRoles.Admin.toString());
        return rootUser;
    }

    public boolean nameIsValid(String s)
    {
        if (userRepository.countByUsername(s) == 0){return true;}
        return false;
    }
}
