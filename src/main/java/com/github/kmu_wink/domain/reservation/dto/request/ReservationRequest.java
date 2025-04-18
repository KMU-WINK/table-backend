package com.github.kmu_wink.domain.reservation.dto.request;

import com.github.kmu_wink.domain.reservation.constant.Space;
import com.github.kmu_wink.domain.reservation.util.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@TimeRange
@FutureDate
public record ReservationRequest(

        @NotNull
        @Size(min = 3, max = 6)
        List<String> participants,

        @NotNull
        Space space,

        @NotNull
        @MaxOneWeek
        LocalDate date,

        @NotNull
        @ReservationTime
        @MinutesDivisibleByTen
        LocalTime startTime,

        @NotNull
        @ReservationTime
        @MinutesDivisibleByTen
        LocalTime endTime,

        @NotBlank
        String reason
) {
}
