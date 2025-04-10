package com.github.kmu_wink.domain.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.domain.reservation.constant.ReservationStatus;
import com.github.kmu_wink.domain.reservation.schema.Reservation;
import com.github.kmu_wink.domain.user.schema.User;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    List<Reservation> findAllByUser(User user);
    List<Reservation> findAllByDate(LocalDate date);
    List<Reservation> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
    Set<Reservation> findAllByStatusInAndDate(Set<ReservationStatus> statuses, LocalDate date);
}
