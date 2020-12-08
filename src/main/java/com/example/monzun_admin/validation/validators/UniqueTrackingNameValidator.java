package com.example.monzun_admin.validation.validators;

import com.example.monzun_admin.repository.TrackingRepository;
import com.example.monzun_admin.validation.rules.UniqueTrackingName;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueTrackingNameValidator implements ConstraintValidator<UniqueTrackingName, String> {
    @Autowired
    private TrackingRepository trackingRepository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return name != null && !trackingRepository.existsByName(name);
    }
}
