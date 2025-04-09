package com.github.kmu_wink.domain.reservation.dto.response;

import com.github.kmu_wink.domain.reservation.constant.Space;
import com.github.kmu_wink.domain.reservation.schema.Reservation;
import com.github.kmu_wink.domain.user.constant.Club;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ReservationFindAllResponse(
        List<ReservationItem> reservations
) {
    @Builder
    public record ReservationItem(
            String reservationId,
            Space space,
            Club club,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        public static ReservationItem from(Reservation reservation) {
            return ReservationItem.builder()
                    .reservationId(reservation.getId())
                    .space(reservation.getSpace())
                    .club(reservation.getClub())
                    .date(reservation.getDate())
                    .startTime(reservation.getStartTime())
                    .endTime(reservation.getEndTime())
                    .build();
        }
    }
}
