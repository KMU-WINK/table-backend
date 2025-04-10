package com.github.kmu_wink.domain.reservation.dto.internal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import com.github.kmu_wink.domain.reservation.constant.ReservationStatus;
import com.github.kmu_wink.domain.reservation.constant.Space;
import com.github.kmu_wink.domain.reservation.schema.Reservation;
import com.github.kmu_wink.domain.user.constant.Club;
import com.github.kmu_wink.domain.user.dto.internal.UserDto;

import lombok.Builder;

@Builder
public record ReservationDto(

	String id,
	Collection<UserDto> participants,
	Club club,
	Space space,
	LocalDate date,
	LocalTime startTime,
	LocalTime endTime,
	String reason,
	ReservationStatus status,
	String returnPicture,
	LocalDateTime returnedAt
) {

	public static ReservationDto from(Reservation reservation) {

		return ReservationDto.builder()
			.id(reservation.getId())
			.participants(reservation.getParticipants().stream().map(UserDto::from).toList())
			.club(reservation.getClub())
			.space(reservation.getSpace())
			.date(reservation.getDate())
			.startTime(reservation.getStartTime())
			.endTime(reservation.getEndTime())
			.reason(reservation.getReason())
			.status(reservation.getStatus())
			.returnPicture(reservation.getReturnPicture())
			.returnedAt(reservation.getReturnedAt())
			.build();
	}
}
