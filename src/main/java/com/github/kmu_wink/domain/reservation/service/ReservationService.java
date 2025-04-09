package com.github.kmu_wink.domain.reservation.service;

import com.github.kmu_wink.domain.reservation.constant.ReservationStatus;
import com.github.kmu_wink.domain.reservation.dto.request.ReservationRequest;
import com.github.kmu_wink.domain.reservation.dto.response.MyReservationResponse;
import com.github.kmu_wink.domain.reservation.dto.response.MyReservationResponse.MyReservationItem;
import com.github.kmu_wink.domain.reservation.dto.response.ReservationFindAllResponse;
import com.github.kmu_wink.domain.reservation.dto.response.ReservationFindAllResponse.ReservationItem;
import com.github.kmu_wink.domain.reservation.repository.ReservationRepository;
import com.github.kmu_wink.domain.reservation.schema.Reservation;
import com.github.kmu_wink.domain.user.schema.User;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public void reserve(User user, ReservationRequest request) {

        Reservation reservation = Reservation.builder()
                .user(user)
                .space(request.space())
                .club(user.getClub())
                .date(request.date())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .peopleCount(request.peopleCount())
                .useReason(request.useReason())
                .status(ReservationStatus.PENDING)
                .build();

        reservationRepository.save(reservation);
    }


    public ReservationFindAllResponse getDailyReservations(LocalDate date) {

        List<ReservationItem> reservations = reservationRepository.findAllByDate(date)
                .stream().map(ReservationItem::from)
                .toList();

        return ReservationFindAllResponse
                .builder()
                .reservations(reservations)
                .build();
    }

    public ReservationFindAllResponse getWeeklyReservations(LocalDate startDate, LocalDate endDate) {

        List<ReservationItem> reservations = reservationRepository.findAllByDateBetween(startDate, endDate)
                .stream().map(ReservationItem::from)
                .toList();

        return ReservationFindAllResponse
                .builder()
                .reservations(reservations)
                .build();
    }

    public MyReservationResponse getReservationsByUser(User user) {

        List<MyReservationItem> reservations = reservationRepository.findAllByUser(user)
                .stream().map(MyReservationItem::from)
                .toList();

        return MyReservationResponse
                .builder()
                .reservations(reservations)
                .build();
    }

    public ReservationFindAllResponse getCurrentReservations() {

        List<ReservationItem> reservations = reservationRepository.findAllByStatus(ReservationStatus.IN_USE)
                .stream().map(ReservationItem::from)
                .toList();

        return ReservationFindAllResponse
                .builder()
                .reservations(reservations)
                .build();
    }
}
