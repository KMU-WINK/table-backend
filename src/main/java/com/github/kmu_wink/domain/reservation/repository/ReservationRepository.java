package com.github.kmu_wink.domain.reservation.repository;

import com.github.kmu_wink.domain.reservation.constant.ReservationStatus;
import com.github.kmu_wink.domain.user.schema.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.domain.reservation.schema.Reservation;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findAllByDate(LocalDate date);
    List<Reservation> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Reservation> findAllByUser(User user);
    List<Reservation> findAllByStatus(ReservationStatus status);
}
