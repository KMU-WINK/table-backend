package com.github.kmu_wink.domain.user.dto.response;

import com.github.kmu_wink.domain.user.schema.User;

import lombok.Builder;

@Builder
public record UserResponse(

	User user
) {
}
