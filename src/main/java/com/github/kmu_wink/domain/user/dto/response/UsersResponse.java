package com.github.kmu_wink.domain.user.dto.response;

import java.util.Collection;

import com.github.kmu_wink.domain.user.dto.internal.UserDto;

import lombok.Builder;

@Builder
public record UsersResponse(

	Collection<UserDto> users
) {
}
