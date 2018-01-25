package com.quantech.misc.UniqueNameValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//Allows us to use @ValidateString(enum) to ensure lists only contain enum values
@Constraint(validatedBy = UniqueStringValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull(message = "Value cannot be null")
@ReportAsSingleViolation
public @interface ValidateStringUnique {

    String message() default "Entry is already taken";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
