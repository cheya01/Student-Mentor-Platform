package com.skill_mentor.root.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return false;

        return password.length() >= 12 &&
                password.matches(".*[A-Z].*") &&    // at least one uppercase
                password.matches(".*[a-z].*") &&    // at least one lowercase
                password.matches(".*\\d.*") &&      // at least one digit
                password.matches(".*[^a-zA-Z0-9].*"); // at least one special character
    }
}
