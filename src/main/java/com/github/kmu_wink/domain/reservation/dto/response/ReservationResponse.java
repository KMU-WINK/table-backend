package com.github.kmu_wink.domain.reservation.dto.response;

import com.github.kmu_wink.domain.reservation.schema.Reservation;

import lombok.Builder;

@Builder
public record ReservationResponse(

    Reservation reservation
) {
}
