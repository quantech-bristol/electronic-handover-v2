package com.quantech.misc.UniqueNameValidation;

import com.quantech.entities.user.UserRepository;
import com.quantech.entities.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

//This class defines how to validate a list contains only enum values for @ValidateString
@Component
public class UniqueStringValidator implements ConstraintValidator<ValidateStringUnique, String> {

    @Autowired
    UserService users;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {
        if (users == null){return true;}
        else if (users.nameIsValid(value)) {return true;}
        return false;
    }



    @Override
    public void initialize(ValidateStringUnique constraintAnnotation)
    {
    }


}
