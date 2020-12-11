package com.example.monzun_admin.validation.validators;

import com.example.monzun_admin.repository.UserRepository;
import com.example.monzun_admin.validation.rules.ExistsUserEmail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistsUserEmailValidator implements ConstraintValidator<ExistsUserEmail, String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return email != null && userRepository.findByEmail(email) != null;
    }
}
