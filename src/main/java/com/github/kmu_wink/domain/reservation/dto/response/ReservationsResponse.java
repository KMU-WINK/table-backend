package com.github.kmu_wink.domain.reservation.dto.response;

import java.util.Collection;

import com.github.kmu_wink.domain.reservation.dto.internal.ReservationDto;

import lombok.Builder;

@Builder
public record ReservationsResponse(

        Collection<ReservationDto> reservations
) {
}
