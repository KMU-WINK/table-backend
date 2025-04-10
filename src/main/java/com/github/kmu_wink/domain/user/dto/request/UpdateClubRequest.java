package com.github.kmu_wink.domain.user.dto.request;

import com.github.kmu_wink.domain.user.constant.Club;

import jakarta.validation.constraints.NotNull;

public record UpdateClubRequest(

	@NotNull
	Club club
) {
}
