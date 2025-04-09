package com.github.kmu_wink.domain.reservation.dto.response;

import com.github.kmu_wink.domain.reservation.constant.ReservationStatus;
import com.github.kmu_wink.domain.reservation.constant.Space;
import com.github.kmu_wink.domain.reservation.schema.Reservation;
import com.github.kmu_wink.domain.user.constant.Club;
import com.github.kmu_wink.domain.user.schema.User;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
            Set<String> participants,
            String useReason,
            ReservationStatus status,
            String returnPictureUrl
    ) {
        public static MyReservationItem from(Reservation reservation) {
            return MyReservationItem.builder()
                    .reservationId(reservation.getId())
                    .space(reservation.getSpace())
                    .club(reservation.getClub())
                    .date(reservation.getDate())
                    .startTime(reservation.getStartTime())
                    .endTime(reservation.getEndTime())
                    .participants(reservation.getParticipants()
                            .stream()
                            .map(User::getName)
                            .collect(Collectors.toUnmodifiableSet())
                    )
                    .useReason(reservation.getUseReason())
                    .status(reservation.getStatus())
                    .returnPictureUrl(reservation.getReturnPictureUrl())
                    .build();
        }
    }
}
