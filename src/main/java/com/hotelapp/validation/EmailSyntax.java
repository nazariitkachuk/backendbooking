package com.hotelapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
/**
 * Validates whether string is valid email address- has correct syntax
 */
@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailSyntax {


    String message() default "{Wrong email name}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
