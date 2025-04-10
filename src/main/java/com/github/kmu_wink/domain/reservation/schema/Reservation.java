package com.github.kmu_wink.domain.reservation.schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.kmu_wink.common.database.BaseSchema;
import com.github.kmu_wink.domain.reservation.constant.ReservationStatus;
import com.github.kmu_wink.domain.reservation.constant.Space;
import com.github.kmu_wink.domain.user.constant.Club;
import com.github.kmu_wink.domain.user.schema.User;

import jakarta.annotation.Nullable;
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
	@JsonIgnore
	User user;

	@DBRef
	Set<User> participants;

	Club club;

	Space space;

	LocalDate date;
	LocalTime startTime;
	LocalTime endTime;

	String reason;

	ReservationStatus status;

	@Nullable
	String returnPicture;

	@Nullable
	LocalDateTime returnedAt;
}
