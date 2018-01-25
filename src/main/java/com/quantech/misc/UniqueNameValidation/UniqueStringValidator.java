package com.quantech.misc.UniqueNameValidation;

import com.quantech.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

//This class defines how to validate a list contains only enum values for @ValidateString
public class UniqueStringValidator implements ConstraintValidator<ValidateStringUnique, String> {

    @Autowired
    UserRepository users;
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {
        if (users.findUserCoreByUsername(value) != null) {return false;}
        return true;
    }



    @Override
    public void initialize(ValidateStringUnique constraintAnnotation)
    {
    }


}
