package com.github.kmu_wink.domain.user.dto.response;

import com.github.kmu_wink.domain.user.dto.internal.UserDto;

import lombok.Builder;

@Builder
public record UserResponse(

	UserDto user
) {
}
