package com.github.kmu_wink.domain.reservation.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.domain.reservation.schema.Reservation;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {


}
