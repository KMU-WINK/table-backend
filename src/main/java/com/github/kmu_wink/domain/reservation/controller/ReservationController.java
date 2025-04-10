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
import com.github.kmu_wink.domain.reservation.dto.response.ReservationResponse;
import com.github.kmu_wink.domain.reservation.dto.response.ReservationsResponse;
import com.github.kmu_wink.domain.reservation.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@ApiController("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/me")
    public ApiResponse<ReservationsResponse> getMyReservations(
        @AuthenticationPrincipal OAuth2GoogleUser principal
    ) {

        return ApiResponse.ok(reservationService.getMyReservations(principal.getUser()));
    }

    @GetMapping("/daily")
    public ApiResponse<ReservationsResponse> getDailyReservations(
        @RequestParam LocalDate date
    ) {

        return ApiResponse.ok(reservationService.getDailyReservations(date));
    }

    @GetMapping("/weekly")
    public ApiResponse<ReservationsResponse> getWeeklyReservations(
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate
    ) {

        return ApiResponse.ok(reservationService.getWeeklyReservations(startDate, endDate));
    }

    @PostMapping
    public ApiResponse<ReservationResponse> reserve(
        @AuthenticationPrincipal OAuth2GoogleUser principal,
        @RequestBody @Valid ReservationRequest request
    ) {

        return ApiResponse.ok(reservationService.reserve(principal.getUser(), request));
    }

    @PostMapping("/{reservationId}/return")
    public ApiResponse<ReservationResponse> returnReservation(
        @AuthenticationPrincipal OAuth2GoogleUser principal,
        @PathVariable String reservationId,
        @RequestPart MultipartFile file
    ) {

        return ApiResponse.ok(reservationService.returnReservation(principal.getUser(), reservationId, file));
    }
}
