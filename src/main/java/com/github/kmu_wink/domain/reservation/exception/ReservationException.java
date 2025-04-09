package com.github.kmu_wink.domain.reservation.exception;

import com.github.kmu_wink.common.api.exception.ApiException;

public class ReservationException extends ApiException {

	private ReservationException(ReservationExceptions reservationExceptions) {

		super(reservationExceptions.getMessage());
	}

	public static ReservationException of(ReservationExceptions reservationExceptions) {

		return new ReservationException(reservationExceptions);
	}
}
