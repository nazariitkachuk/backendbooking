package com.hotelapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Email;
import java.util.Objects;

/**
 * Validates whether string is valid email address- has correct syntax
 */
public class EmailValidator implements ConstraintValidator<EmailSyntax,String> {
    @Override
    public boolean isValid(String emailAddress, ConstraintValidatorContext constraintValidatorContext) {

        if( Objects.nonNull(emailAddress)
                && !emailAddress.isEmpty()
                && emailAddress.matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            return true;
        }
        return false;
    }
}
