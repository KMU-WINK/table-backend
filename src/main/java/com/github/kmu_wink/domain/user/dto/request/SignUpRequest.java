package com.github.kmu_wink.domain.user.dto.request;

import com.github.kmu_wink.domain.user.constant.Club;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUpRequest(

	@NotBlank
	String name,

	@NotNull
	Club club
) {
}
