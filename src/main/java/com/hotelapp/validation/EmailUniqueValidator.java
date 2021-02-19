package com.hotelapp.validation;

import com.hotelapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Validates whether email has valid syntax and not exist in database
 */
@Validated
public class EmailUniqueValidator implements ConstraintValidator<EmailUnique,String> {

    @Autowired
    @NotNull
    UserRepository userRepository;

    @Override
    public void initialize(EmailUnique constraintAnnotation) {

    }

    @Override
    public boolean isValid(String emailAddress, ConstraintValidatorContext constraintValidatorContext) {
        if( Objects.nonNull(emailAddress)
                && !emailAddress.isEmpty()
                && emailAddress.matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            if(userRepository.existsByEmail(emailAddress)){
                return false;
            }
            else

                   return true;
        }
        else {
            return false;
        }
    }
}
