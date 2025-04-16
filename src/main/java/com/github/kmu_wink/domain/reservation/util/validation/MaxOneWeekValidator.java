package com.github.kmu_wink.domain.reservation.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class MaxOneWeekValidator implements ConstraintValidator<MaxOneWeek, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {

        if (value == null) return true;

        LocalDate today = LocalDate.now();

        return !value.isAfter(today.plusDays(7));
    }
}