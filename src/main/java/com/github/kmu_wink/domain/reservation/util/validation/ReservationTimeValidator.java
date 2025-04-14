package com.github.kmu_wink.domain.reservation.util.validation;

import java.time.LocalTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReservationTimeValidator implements ConstraintValidator<ReservationTime, LocalTime> {

	private final LocalTime MINIMUM_TIME = LocalTime.of(9, 0);
	private final LocalTime MAXIMUM_TIME = LocalTime.of(23, 0);

	@Override
	public boolean isValid(LocalTime value, ConstraintValidatorContext context) {

		if (value == null) return true;

		return !value.isBefore(MINIMUM_TIME) && !value.isAfter(MAXIMUM_TIME);
	}
}