package com.github.kmu_wink.domain.reservation.util.validation;

import java.time.LocalDateTime;

import com.github.kmu_wink.domain.reservation.dto.request.ReservationRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FutureDateValidator implements ConstraintValidator<FutureDate, ReservationRequest> {

	@Override
	public boolean isValid(ReservationRequest request, ConstraintValidatorContext context) {

		if (request == null) return true;
		if (request.date() == null || request.startTime() == null) return true;

		LocalDateTime reservationDate = LocalDateTime.of(request.date(), request.startTime());
		LocalDateTime now = LocalDateTime.now();

		return !reservationDate.isBefore(now);
	}
}