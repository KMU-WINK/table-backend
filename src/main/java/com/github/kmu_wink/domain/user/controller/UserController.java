package com.github.kmu_wink.domain.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.kmu_wink.common.api.ApiController;
import com.github.kmu_wink.common.api.ApiResponse;
import com.github.kmu_wink.common.security.oauth2.OAuth2GoogleUser;
import com.github.kmu_wink.domain.user.dto.request.SignUpRequest;
import com.github.kmu_wink.domain.user.dto.request.UpdateClubRequest;
import com.github.kmu_wink.domain.user.dto.response.UserResponse;
import com.github.kmu_wink.domain.user.dto.response.UsersResponse;
import com.github.kmu_wink.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@ApiController("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public ApiResponse<UsersResponse> getAllUser(
		@RequestParam String query
	) {

		return ApiResponse.ok(userService.getAllUser(query));
	}

	@GetMapping("/me")
	public ApiResponse<UserResponse> getMyInfo(
		@AuthenticationPrincipal OAuth2GoogleUser oauthUser
	) {

		return ApiResponse.ok(userService.getMyInfo(oauthUser.getUser()));
	}

	@PostMapping("/sign-up")
	public ApiResponse<UserResponse> signUp(
		@AuthenticationPrincipal OAuth2GoogleUser oauthUser,
		@RequestBody @Valid SignUpRequest request
	) {

		return ApiResponse.ok(userService.signUp(oauthUser.getUser(), request));
	}

	@PutMapping("/club")
	public ApiResponse<UserResponse> updateClub(
		@AuthenticationPrincipal OAuth2GoogleUser oauthUser,
		@RequestBody @Valid UpdateClubRequest request
	) {

		return ApiResponse.ok(userService.updateClub(oauthUser.getUser(), request));
	}
}
