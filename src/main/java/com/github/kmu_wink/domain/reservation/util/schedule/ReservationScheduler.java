package com.github.kmu_wink.domain.reservation.util.schedule;

import static com.github.kmu_wink.domain.reservation.constant.ReservationStatus.*;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.kmu_wink.domain.reservation.constant.ReservationStatus;
import com.github.kmu_wink.domain.reservation.repository.ReservationRepository;
import com.github.kmu_wink.domain.reservation.schema.Reservation;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationRepository reservationRepository;

    private final Set<ReservationStatus> UPDATE_STATUS = Set.of(PENDING, IN_USE);

    @PostConstruct
    @Transactional
    public void onStartUp() {

        LocalDateTime now = LocalDateTime.now();

        reservationRepository.findAllByStatusIn(UPDATE_STATUS)
            .forEach((reservation) -> updateReservationStatus(reservation, now));
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void cron() {

        LocalDateTime now = LocalDateTime.now();

        reservationRepository.findAllByStatusInAndDate(UPDATE_STATUS, now.toLocalDate())
            .forEach((reservation) -> updateReservationStatus(reservation, now));
    }

    @Transactional
    protected void updateReservationStatus(Reservation reservation, LocalDateTime current) {

        LocalDateTime startTime = LocalDateTime.of(reservation.getDate(), reservation.getStartTime());
        LocalDateTime endTime = LocalDateTime.of(reservation.getDate(), reservation.getEndTime());

        if (reservation.getStatus() == PENDING && current.isAfter(startTime)) {

            reservation.setStatus(IN_USE);
            reservationRepository.save(reservation);
        }

        if (reservation.getStatus() == IN_USE && current.isAfter(endTime)) {

            reservation.setStatus(RETURNED);
            reservation.setReturnedAt(LocalDateTime.now());
            reservationRepository.save(reservation);
        }
    }
}