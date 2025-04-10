package com.github.kmu_wink.domain.reservation.dto.response;

import java.util.Collection;

import com.github.kmu_wink.domain.reservation.schema.Reservation;

import lombok.Builder;

@Builder
public record ReservationsResponse(

        Collection<Reservation> reservations
) {
}
