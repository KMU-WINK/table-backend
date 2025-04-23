package com.github.kmu_wink.domain.reservation.service;

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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.kmu_wink.domain.reservation.exception.ReservationExceptions.DUPLICATE_RESERVATION;
import static com.github.kmu_wink.domain.reservation.exception.ReservationExceptions.NOT_PARTICIPANT_RESERVATION;
import static com.github.kmu_wink.domain.reservation.exception.ReservationExceptions.RESERVATION_ALREADY_RETURNED;
import static com.github.kmu_wink.domain.reservation.exception.ReservationExceptions.RESERVATION_ALREADY_STARTED;
import static com.github.kmu_wink.domain.user.exception.UserExceptions.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    private final S3Service s3Service;

    public ReservationsResponse getMyReservations(User user) {

        Sort sort = Sort.by(
                Sort.Order.asc("status"),
                Sort.Order.asc("date"),
                Sort.Order.asc("startTime")
        );

        List<ReservationDto> reservations = reservationRepository.findAllByUser(user, sort).stream()
                .sorted(Comparator
                        .comparing((Reservation res) -> {
                            if (res.getStatus() == ReservationStatus.RETURNED) {
                                return res.getReturnPicture() != null ? 1 : 0;
                            }
                            return 0;
                        })
                        .thenComparing(res -> {
                            LocalDateTime now = LocalDateTime.now();
                            LocalDateTime reservationTime = LocalDateTime.of(res.getDate(), res.getStartTime());
                            return Duration.between(now, reservationTime).abs();
                        })
                )
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

    @Synchronized
    @Transactional
    public ReservationResponse reserve(User user, ReservationRequest dto) {

        reservationRepository.findByDuplicated(dto.space(), dto.date(), dto.endTime(), dto.startTime())
                .ifPresent((ignored) -> {
                    throw ReservationException.of(DUPLICATE_RESERVATION);
                });

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

    public void cancelReservation(User user, String reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .stream()
                .peek(x -> {
                    if (!x.getParticipants().contains(user))
                        throw ReservationException.of(NOT_PARTICIPANT_RESERVATION);
                })
                .peek(x -> {
                    if (!x.getStatus().equals(ReservationStatus.PENDING))
                        throw ReservationException.of(RESERVATION_ALREADY_STARTED);
                })
                .findFirst()
                .orElseThrow();

        reservationRepository.delete(reservation);
    }

    public ReservationResponse returnReservation(User user, String reservationId, MultipartFile file) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .stream()
                .peek(x -> {
                    if (!x.getParticipants().contains(user))
                        throw ReservationException.of(NOT_PARTICIPANT_RESERVATION);
                })
                .peek(x -> {
                    if (x.getReturnPicture() != null)
                        throw ReservationException.of(RESERVATION_ALREADY_RETURNED);
                })
                .findFirst()
                .orElseThrow();

        String returnPictureUrl = s3Service.upload("reservation/return/" + reservationId, file);
        reservation.setReturnPicture(returnPictureUrl);

        if (reservation.getStatus() != ReservationStatus.RETURNED) {

            reservation.setStatus(ReservationStatus.RETURNED);
            reservation.setReturnedAt(LocalDateTime.now());
        }

        reservation = reservationRepository.save(reservation);

        return ReservationResponse.builder()
                .reservation(ReservationDto.from(reservation))
                .build();
    }
}
