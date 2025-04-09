package com.github.kmu_wink.domain.reservation.schema;

import java.time.LocalDateTime;

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

	LocalDateTime startTime;
	LocalDateTime endTime;
	String userReason;
	int peopleCount;
	Space space;
	String returnPictureUrl;
	LocalDateTime returnedAt;
}
