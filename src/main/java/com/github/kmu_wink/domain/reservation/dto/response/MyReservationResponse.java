package com.github.kmu_wink.domain.reservation.dto.response;

import com.github.kmu_wink.domain.reservation.constant.ReservationStatus;
import com.github.kmu_wink.domain.reservation.constant.Space;
import com.github.kmu_wink.domain.reservation.schema.Reservation;
import com.github.kmu_wink.domain.user.schema.User.Club;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;

@Builder
public record MyReservationResponse(
        List<MyReservationItem> reservations
) {
    @Builder
    public record MyReservationItem(
            String reservationId,
            Space space,
            Club club,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            Integer peopleCount,
            String useReason,
            ReservationStatus status,
            String returnPictureUrl
    ) {
        public static MyReservationItem from(Reservation reservation) {
            return MyReservationItem.builder()
                    .reservationId(reservation.getId())
                    .space(reservation.getSpace())
                    .club(reservation.getClub())
                    .peopleCount(reservation.getPeopleCount())
                    .useReason(reservation.getUseReason())
                    .status(reservation.getStatus())
                    .returnPictureUrl(reservation.getReturnPictureUrl())
                    .build();
        }
    }
}
