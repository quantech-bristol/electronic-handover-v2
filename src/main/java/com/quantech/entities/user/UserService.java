package com.quantech.entities.user;

import com.quantech.Configurations.SecurityRoles;
import com.quantech.misc.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    public UserService() {

    }
    @PostConstruct
    public void test()
    {
        if( userRepository.count() == 0)
        {
            // Commented out for unit testing:
            // userRepository.save(new UserCore("quantech","quantech", SecurityRoles.Admin, Title.Mx, "quan", "tech", "quantech@gmail.com"));

        }
    }

    public void delUser(String user) {
        userRepository.deleteUserCoreByUsername(user);test();
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
        UserCore newUser = userRepository.findUserCoreByUsername(s);
        if (newUser==null) {
            throw new UsernameNotFoundException(s);
        }
        return newUser;
    }

    private UserCore rootUser() {

        UserCore rootUser = new UserCore("quantech","quantech", SecurityRoles.Admin, Title.Mx, "quan", "tech", "quantech@gmail.com");
        return rootUser;
    }

    public boolean nameIsValid(String s, Long id)
    {
        UserCore user = userRepository.findUserCoreByUsername(s);
        if ((user == null)||(user.getId() == id)){return true;}
        return false;
    }
    public boolean emailIsValid(String s, Long id)
    {
        UserCore user = userRepository.findUserCoreByEmail(s);
        if ((user == null)||(user.getId() == id)){return true;}
        return false;
    }
}
