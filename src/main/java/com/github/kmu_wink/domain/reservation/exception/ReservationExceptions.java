package com.github.kmu_wink.domain.reservation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationExceptions {

	RESERVATION_NOT_FOUND("예약을 찾을 수 없습니다."),
	NOT_PARTICIPANT_RESERVATION("예약에 참여하지 않은 사용자입니다."),
	RESERVATION_ALREADY_RETURNED("이미 반납된 예약입니다."),
	;

	private final String message;
}
