package com.github.kmu_wink.domain.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.domain.reservation.constant.ReservationStatus;
import com.github.kmu_wink.domain.reservation.constant.Space;
import com.github.kmu_wink.domain.reservation.schema.Reservation;
import com.github.kmu_wink.domain.user.schema.User;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    @Query("{ 'space': ?0, 'date': ?1, 'status': { $ne: 'RETURNED' }, 'startTime': { $lt: ?2 }, 'endTime': { $gt: ?3 } }")
    Optional<Reservation> findByDuplicated(Space space, LocalDate date, LocalTime endTime, LocalTime startTime);

    List<Reservation> findAllByUser(User user, Sort sort);
    List<Reservation> findAllByDate(LocalDate date);
    List<Reservation> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
    Set<Reservation> findAllByStatusInAndDate(Set<ReservationStatus> statuses, LocalDate date);
    Set<Reservation> findAllByStatusIn(Set<ReservationStatus> statuses);
}
