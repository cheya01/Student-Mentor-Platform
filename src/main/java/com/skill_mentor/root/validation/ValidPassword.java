package com.skill_mentor.root.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Password must be at least 12 characters long and contain uppercase, lowercase, number, and symbol";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
