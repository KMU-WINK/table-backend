package com.github.kmu_wink.domain.reservation.schema;

import com.github.kmu_wink.domain.reservation.constant.ReservationStatus;
import com.github.kmu_wink.domain.user.schema.User.Club;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.LocalTime;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.github.kmu_wink.common.database.BaseSchema;
import com.github.kmu_wink.domain.reservation.constant.Space;
import com.github.kmu_wink.domain.user.schema.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Reservation extends BaseSchema {

	@DBRef
	User user;
	Club club;
	LocalDate date;
	LocalTime startTime;
	LocalTime endTime;
	String useReason;
	int peopleCount;
	Space space;
	ReservationStatus status;
	String returnPictureUrl;
	LocalDateTime returnedAt;
}
