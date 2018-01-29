package com.quantech.misc.StringEnumValidation;

import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;


//Allows us to use @ValidateListEnumValues(enum) to ensure lists only contain enum values
@Constraint(validatedBy = ListEnumValuesValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull(message = "Value cannot be null")
@ReportAsSingleViolation
public @interface ValidateListEnumValues {
    Class<? extends Enum<?>> enumClazz();

    String message() default "String does not match any known enum";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
