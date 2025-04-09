package com.github.kmu_wink.domain.reservation.service;

import com.github.kmu_wink.domain.reservation.constant.ReservationStatus;
import com.github.kmu_wink.domain.reservation.repository.ReservationRepository;
import com.github.kmu_wink.domain.reservation.schema.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        LocalTime currentTime = now.toLocalTime();
        LocalTime lastMinute = currentTime.minusMinutes(1);
        LocalTime nextMinute = currentTime.plusMinutes(1);
        Set<Reservation> reservations = reservationRepository.findAllByStatusInAndDate(UPDATE_STATUS, today);

        reservations.forEach(reservation -> {

            if (reservation.getDate().isEqual(today)) {
                if (reservation.getStatus() == ReservationStatus.PENDING &&
                        reservation.getStartTime().isAfter(lastMinute) && currentTime.isBefore(reservation.getEndTime())) {

                    reservation.setStatus(ReservationStatus.IN_USE);
                }

                if (reservation.getStatus() == ReservationStatus.IN_USE && reservation.getEndTime().isAfter(lastMinute) && nextMinute.isAfter(reservation.getEndTime())) {

                    reservation.setStatus(ReservationStatus.RETURNED);
                }

                reservationRepository.save(reservation);
            }
        });
    }
}
