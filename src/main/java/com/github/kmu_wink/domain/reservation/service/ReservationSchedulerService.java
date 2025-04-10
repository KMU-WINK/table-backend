package com.github.kmu_wink.domain.reservation.service;

import com.github.kmu_wink.domain.reservation.constant.ReservationStatus;
import com.github.kmu_wink.domain.reservation.repository.ReservationRepository;
import com.github.kmu_wink.domain.reservation.schema.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationSchedulerService {

    private final ReservationRepository reservationRepository;
    private static final Set<ReservationStatus> UPDATE_STATUS = Set.of(
            ReservationStatus.PENDING,
            ReservationStatus.IN_USE
    );

    @Scheduled(cron = "0 * * * * *")
    public void updateReservationStatus() {

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        Set<Reservation> reservations = reservationRepository.findAllByStatusInAndDate(UPDATE_STATUS, today);

        reservations.forEach(reservation -> {
            LocalDateTime startTime = LocalDateTime.of(reservation.getDate(), reservation.getStartTime());
            LocalDateTime endTime = LocalDateTime.of(reservation.getDate(), reservation.getEndTime());
            ReservationStatus lastStatus = reservation.getStatus();

            if (lastStatus == ReservationStatus.PENDING && startTime.isBefore(now)
                    && endTime.isAfter(now)) {

                reservation.setStatus(ReservationStatus.IN_USE);
            }

            if (lastStatus == ReservationStatus.IN_USE && endTime.isBefore(now)) {

                reservation.setStatus(ReservationStatus.RETURNED);
            }


            if (!lastStatus.equals(reservation.getStatus())) {
                reservationRepository.save(reservation);
            }
        });
    }
}
