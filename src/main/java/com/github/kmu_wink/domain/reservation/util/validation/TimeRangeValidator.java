package com.github.kmu_wink.domain.reservation.util.validation;

import com.github.kmu_wink.domain.reservation.dto.request.ReservationRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TimeRangeValidator implements ConstraintValidator<TimeRange, ReservationRequest> {

	@Override
	public boolean isValid(ReservationRequest request, ConstraintValidatorContext context) {

		if (request == null) return true;
		if (request.startTime() == null || request.endTime() == null) return true;

		return request.endTime().isAfter(request.startTime());
	}
}