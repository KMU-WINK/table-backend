package com.github.kmu_wink.domain.reservation.service;

import static com.github.kmu_wink.domain.reservation.exception.ReservationExceptions.*;
import static com.github.kmu_wink.domain.user.exception.UserExceptions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.kmu_wink.common.external.aws.s3.S3Service;
import com.github.kmu_wink.domain.reservation.constant.ReservationStatus;
import com.github.kmu_wink.domain.reservation.dto.internal.ReservationDto;
import com.github.kmu_wink.domain.reservation.dto.request.ReservationRequest;
import com.github.kmu_wink.domain.reservation.dto.response.ReservationResponse;
import com.github.kmu_wink.domain.reservation.dto.response.ReservationsResponse;
import com.github.kmu_wink.domain.reservation.exception.ReservationException;
import com.github.kmu_wink.domain.reservation.repository.ReservationRepository;
import com.github.kmu_wink.domain.reservation.schema.Reservation;
import com.github.kmu_wink.domain.user.exception.UserException;
import com.github.kmu_wink.domain.user.repository.UserRepository;
import com.github.kmu_wink.domain.user.schema.User;

import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    private final S3Service s3Service;

    public ReservationsResponse getMyReservations(User user) {

        List<ReservationDto> reservations = reservationRepository.findAllByUser(user).stream()
            .map(ReservationDto::from)
            .toList();

        return ReservationsResponse.builder()
            .reservations(reservations)
            .build();
    }

    public ReservationsResponse getDailyReservations(LocalDate date) {

        List<ReservationDto> reservations = reservationRepository.findAllByDate(date).stream()
            .map(ReservationDto::from)
            .toList();

        return ReservationsResponse.builder()
            .reservations(reservations)
            .build();
    }

    public ReservationsResponse getWeeklyReservations(LocalDate startDate, LocalDate endDate) {

        List<ReservationDto> reservations = reservationRepository.findAllByDateBetween(startDate, endDate).stream()
            .map(ReservationDto::from)
            .toList();

        return ReservationsResponse.builder()
            .reservations(reservations)
            .build();
    }

    public ReservationResponse reserve(User user, ReservationRequest dto) {

        Set<User> participants = Stream.of(dto.participants(), List.of(user.getId()))
            .flatMap(List::stream)
            .distinct()
            .map(userRepository::findById)
            .map(x -> x.orElseThrow(() -> UserException.of(USER_NOT_FOUND)))
            .collect(Collectors.toSet());

        Reservation reservation = Reservation.builder()
            .user(user)
            .participants(participants)
            .club(user.getClub())
            .space(dto.space())
            .date(dto.date())
            .startTime(dto.startTime())
            .endTime(dto.endTime())
            .reason(dto.reason())
            .status(ReservationStatus.PENDING)
            .build();

        reservation = reservationRepository.save(reservation);

        return ReservationResponse.builder()
            .reservation(ReservationDto.from(reservation))
            .build();
    }
    
    public ReservationResponse returnReservation(User user, String reservationId, MultipartFile file) {

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
        reservation.setReturnPicture(returnPictureUrl);
        reservation.setReturnedAt(LocalDateTime.now());

        reservation = reservationRepository.save(reservation);

        return ReservationResponse.builder()
            .reservation(ReservationDto.from(reservation))
            .build();
    }
}
