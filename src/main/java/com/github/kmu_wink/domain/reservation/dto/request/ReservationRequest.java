package com.github.kmu_wink.domain.reservation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.github.kmu_wink.domain.reservation.constant.Space;

public record ReservationRequest(

	List<String> participants,

	Space space,

	LocalDate date,
	LocalTime startTime,
	LocalTime endTime,

	String reason
) {
}
