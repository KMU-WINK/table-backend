package com.github.kmu_wink.domain.user.dto.internal;

import com.github.kmu_wink.domain.user.constant.Club;
import com.github.kmu_wink.domain.user.schema.User;

import lombok.Builder;

@Builder
public record UserDto(

	String id,
	Club club,
	String name
) {

	public static UserDto from(User user) {

		return UserDto.builder()
			.id(user.getId())
			.club(user.getClub())
			.name(user.getName())
			.build();
	}
}
