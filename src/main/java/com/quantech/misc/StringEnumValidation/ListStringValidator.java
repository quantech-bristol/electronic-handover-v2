package com.quantech.misc.StringEnumValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

//This class defines how to validate a list contains only enum values for @ValidateString
public class ListStringValidator implements ConstraintValidator<ValidateString, List<String>> {
    private List<String> acceptedVals;

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context)
    {
        for (String s:value)
        {
            if(!acceptedVals.contains(s.toUpperCase())) {return false;}
        }
        return true;
    }



    @Override
    public void initialize(ValidateString constraintAnnotation)
    {
        acceptedVals = new ArrayList<String>();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClazz();

        @SuppressWarnings("rawtypes")
        Enum[] enumValArr = enumClass.getEnumConstants();

        for(@SuppressWarnings("rawtypes") Enum enumVal : enumValArr)
        {
            acceptedVals.add(enumVal.toString().toUpperCase());
        }

    }


}
