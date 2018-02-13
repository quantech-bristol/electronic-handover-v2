package com.quantech.oldEntities.user;

import com.quantech.Configurations.SecurityRoles;
import com.quantech.misc.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

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


    @Value("${spring.profiles.active}")
    private String activeProfile;

    @PostConstruct
    public void insertRootUser()
    {
        if ((!activeProfile.matches("test")) && (userRepository.count() == 0))
        {
            userRepository.save(new UserCore("quantech","quantech", SecurityRoles.Admin, Title.Mx, "quan", "tech", "quantech@gmail.com"));
        }
    }

    public void delUser(String user) {
        userRepository.deleteUserCoreByUsername(user);insertRootUser();
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

    public List<UserCore> getAllUsers() {
        List<UserCore> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
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

    public void CheckValidity(BindingResult result, boolean creating, UserFormBackingObject ob)
    {
        if(!nameIsValid(ob.getUsername(),ob.getId()))//If Username is already in use (but not by us)
        {
            result.rejectValue("username","error.usercore","That username is already in use!");//Add an error
        }
        if(!emailIsValid(ob.getEmail(),ob.getId()))//If Username is already in use (but not by us)
        {
            result.rejectValue("email","email.usercore","That email is already in use!");//Add an error
        }
        if ((creating) && (4 > ob.getPassword().length()|| ob.getPassword().length()>20))
        {
            result.rejectValue("password","password.usercore","Passwords should be between 4 and 20 characters!");//Add an error
        }
        if ((ob.getAuthorityStrings().size() == 0)&&(creating))
        {
            result.rejectValue("authorityStrings","authorityStrings.usercore","Surely they have some role in this hospital!");//Add an error
        }
        if (ob.getEmail().length() == 0)
        {
            result.rejectValue("email","email.usercore","You must specify an email.");
        }

    }
}
