package com.github.kmu_wink.domain.reservation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationExceptions {

	RESERVATION_NOT_FOUND("예약 정보를 찾을 수 없습니다.")
	;

	private final String message;
}
