package com.github.kmu_wink.domain.reservation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.github.kmu_wink.domain.reservation.constant.Space;
import com.github.kmu_wink.domain.reservation.util.validation.MinutesDivisibleByTen;
import com.github.kmu_wink.domain.reservation.util.validation.ReservationTime;
import com.github.kmu_wink.domain.reservation.util.validation.TimeRange;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@TimeRange
public record ReservationRequest(

	@NotNull
	List<String> participants,

	@NotNull
	Space space,

	@NotNull
	LocalDate date,

	@NotNull
	@ReservationTime
	@MinutesDivisibleByTen
	LocalTime startTime,

	@NotNull
	@ReservationTime
	@MinutesDivisibleByTen
	LocalTime endTime,

	@NotBlank
	String reason
) {
}
