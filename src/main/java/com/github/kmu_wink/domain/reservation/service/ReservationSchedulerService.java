package com.github.kmu_wink.domain.reservation.service;

import static com.github.kmu_wink.domain.reservation.constant.ReservationStatus.*;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.kmu_wink.domain.reservation.constant.ReservationStatus;
import com.github.kmu_wink.domain.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationSchedulerService {

    private final ReservationRepository reservationRepository;

    private static final Set<ReservationStatus> UPDATE_STATUS = Set.of(PENDING, IN_USE);

    @Scheduled(cron = "0 * * * * *")
    public void updateReservationStatus() {

        LocalDateTime now = LocalDateTime.now();

        reservationRepository.findAllByStatusInAndDate(UPDATE_STATUS, now.toLocalDate())
            .forEach(reservation -> {

                LocalDateTime startTime = LocalDateTime.of(reservation.getDate(), reservation.getStartTime());
                LocalDateTime endTime = LocalDateTime.of(reservation.getDate(), reservation.getEndTime());

                if (reservation.getStatus() == PENDING && now.isAfter(startTime)) {

                    reservation.setStatus(IN_USE);
                }

                if (reservation.getStatus() == IN_USE && now.isAfter(endTime)) {

                    reservation.setStatus(RETURNED);
                }

                reservationRepository.save(reservation);
            });
    }
}
