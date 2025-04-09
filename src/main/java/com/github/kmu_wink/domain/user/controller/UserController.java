package com.github.kmu_wink.domain.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.kmu_wink.common.api.ApiController;
import com.github.kmu_wink.common.api.ApiResponse;
import com.github.kmu_wink.common.security.oauth2.OAuth2GoogleUser;
import com.github.kmu_wink.domain.user.dto.request.SignUpRequest;
import com.github.kmu_wink.domain.user.dto.response.UserResponse;
import com.github.kmu_wink.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@ApiController("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public ApiResponse<UserResponse> me(
		@AuthenticationPrincipal OAuth2GoogleUser oauthUser
	) {

		return ApiResponse.ok(userService.me(oauthUser));
	}

	@PutMapping
	public ApiResponse<UserResponse> signUp(
		@AuthenticationPrincipal OAuth2GoogleUser oauthUser,
		@RequestBody @Valid SignUpRequest request
	) {

		return ApiResponse.ok(userService.signUp(oauthUser, request));
	}
}
