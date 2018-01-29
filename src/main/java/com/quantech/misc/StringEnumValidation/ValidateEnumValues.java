package com.quantech.misc.StringEnumValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//Allows us to use @ValidateListEnumValues(enum) to ensure lists only contain enum values
@Constraint(validatedBy = EnumValuesValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull(message = "Value cannot be null")
@ReportAsSingleViolation
public @interface ValidateEnumValues {
    Class<? extends Enum<?>> enumClazz();

    String message() default "String does not match any known enum";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
