package com.hotelapp.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
/**
 * Validates whether email has valid syntax and not exist in database
 */
@Documented
@Constraint(validatedBy = EmailUniqueValidator.class)
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailUnique {


    String message() default "{Email has wrong name or user with email exists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
