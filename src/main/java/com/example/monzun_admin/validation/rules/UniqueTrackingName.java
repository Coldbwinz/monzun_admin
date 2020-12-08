package com.example.monzun_admin.validation.rules;

import com.example.monzun_admin.validation.validators.UniqueTrackingNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueTrackingNameValidator.class)
@Documented
public @interface UniqueTrackingName {
    String message() default "Exist tracking with this name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
