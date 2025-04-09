package com.github.kmu_wink.domain.reservation.service;

import static com.github.kmu_wink.domain.reservation.exception.ReservationExceptions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.kmu_wink.common.external.aws.s3.S3Service;
import com.github.kmu_wink.domain.reservation.constant.ReservationStatus;
import com.github.kmu_wink.domain.reservation.dto.request.ReservationRequest;
import com.github.kmu_wink.domain.reservation.dto.response.MyReservationResponse;
import com.github.kmu_wink.domain.reservation.dto.response.MyReservationResponse.MyReservationItem;
import com.github.kmu_wink.domain.reservation.dto.response.ReservationFindAllResponse;
import com.github.kmu_wink.domain.reservation.dto.response.ReservationFindAllResponse.ReservationItem;
import com.github.kmu_wink.domain.reservation.exception.ReservationException;
import com.github.kmu_wink.domain.reservation.repository.ReservationRepository;
import com.github.kmu_wink.domain.reservation.schema.Reservation;
import com.github.kmu_wink.domain.user.exception.UserException;
import com.github.kmu_wink.domain.user.exception.UserExceptions;
import com.github.kmu_wink.domain.user.repository.UserRepository;
import com.github.kmu_wink.domain.user.schema.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public void reserve(User user, ReservationRequest request) {

        Set<User> participants = request.participantIds()
                .stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> UserException.of(UserExceptions.USER_NOT_FOUND)))
                .collect(Collectors.toUnmodifiableSet());

        Reservation reservation = Reservation.builder()
                .user(user)
                .space(request.space())
                .club(user.getClub())
                .date(request.date())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .useReason(request.useReason())
                .participants(participants)
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

    public void returnReservation(User user, String reservationId, MultipartFile file) {

        Reservation reservation = reservationRepository.findById(reservationId)
            .stream()
            .peek(x -> {
                if (!x.getParticipants().contains(user))
                    throw ReservationException.of(NOT_PARTICIPANT_RESERVATION);
            })
            .peek(x -> {
                if (x.getStatus().equals(ReservationStatus.RETURNED))
                    throw ReservationException.of(RESERVATION_ALREADY_RETURNED);
            })
            .findFirst()
            .orElseThrow();

        String returnPictureUrl = s3Service.upload("reservation/return/" + reservationId, file);

        reservation.setStatus(ReservationStatus.RETURNED);
        reservation.setReturnPictureUrl(returnPictureUrl);
        reservation.setReturnedAt(LocalDateTime.now());

        reservationRepository.save(reservation);
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
