package com.github.kmu_wink.domain.reservation.dto.response;

import com.github.kmu_wink.domain.reservation.dto.internal.ReservationDto;

import lombok.Builder;

@Builder
public record ReservationResponse(

	ReservationDto reservation
) {
}
