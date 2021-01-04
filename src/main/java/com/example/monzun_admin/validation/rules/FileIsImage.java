package com.example.monzun_admin.validation.rules;

import com.example.monzun_admin.validation.validators.FileIsImageValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Constraint(validatedBy = FileIsImageValidator.class)
@Documented
public @interface FileIsImage {
    String message() default "File should be an image";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
