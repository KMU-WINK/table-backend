package com.github.kmu_wink.domain.reservation.controller;

import java.time.LocalDate;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.github.kmu_wink.common.api.ApiController;
import com.github.kmu_wink.common.api.ApiResponse;
import com.github.kmu_wink.common.security.oauth2.OAuth2GoogleUser;
import com.github.kmu_wink.domain.reservation.dto.request.ReservationRequest;
import com.github.kmu_wink.domain.reservation.dto.response.MyReservationResponse;
import com.github.kmu_wink.domain.reservation.dto.response.ReservationFindAllResponse;
import com.github.kmu_wink.domain.reservation.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@ApiController
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/reservations")
    public ApiResponse<Void> reserve(
            @AuthenticationPrincipal OAuth2GoogleUser principal,
            @Valid @RequestBody ReservationRequest request
    ) {

        reservationService.reserve(principal.getUser(), request);
        return ApiResponse.ok(null);
    }

    @GetMapping("/reservations/daily")
    public ApiResponse<ReservationFindAllResponse> getDailyReservations(
            @RequestParam LocalDate date
    ) {

        return ApiResponse.ok(reservationService.getDailyReservations(date));
    }

    @GetMapping("/reservations/weekly")
    public ApiResponse<ReservationFindAllResponse> getWeeklyReservations(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {

        return ApiResponse.ok(reservationService.getWeeklyReservations(startDate, endDate));
    }

    @PostMapping("/reservations/{reservationId}/return")
    public ApiResponse<Void> returnReservation(
        @AuthenticationPrincipal OAuth2GoogleUser principal,
        @PathVariable String reservationId,
        @RequestPart MultipartFile file
    ) {

        reservationService.returnReservation(principal.getUser(), reservationId, file);

        return ApiResponse.ok(null);
    }

    @GetMapping("/reservations/me")
    public ApiResponse<MyReservationResponse> getMyReservations(
            @AuthenticationPrincipal OAuth2GoogleUser principal
    ) {

        return ApiResponse.ok(reservationService.getReservationsByUser(principal.getUser()));
    }

    @GetMapping("/reservations/current")
    public ApiResponse<ReservationFindAllResponse> getCurrentReservations() {

        return ApiResponse.ok(reservationService.getCurrentReservations());
    }
}
