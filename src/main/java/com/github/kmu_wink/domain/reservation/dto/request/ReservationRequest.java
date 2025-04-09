package com.github.kmu_wink.domain.reservation.dto.request;

import com.github.kmu_wink.domain.reservation.constant.Space;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ReservationRequest(
        Space space,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String useReason,
        List<String> participantIds
) {
}
