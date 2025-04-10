package com.github.kmu_wink.domain.user.service;

import static com.github.kmu_wink.domain.user.exception.UserExceptions.*;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.github.kmu_wink.common.security.oauth2.OAuth2GoogleUser;
import com.github.kmu_wink.domain.user.dto.request.SignUpRequest;
import com.github.kmu_wink.domain.user.dto.response.UserResponse;
import com.github.kmu_wink.domain.user.exception.UserException;
import com.github.kmu_wink.domain.user.repository.UserRepository;
import com.github.kmu_wink.domain.user.schema.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public UserResponse me(OAuth2GoogleUser oauthUser) {

		return UserResponse.builder()
			.user(oauthUser.getUser())
			.build();
	}

	public UserResponse signUp(OAuth2GoogleUser oauthUser, SignUpRequest dto) {

		User user = oauthUser.getUser();

		if (Objects.nonNull(user.getName()) && Objects.nonNull(user.getClub())) {
			throw UserException.of(ALREADY_SIGNUP);
		}

		user.setName(dto.name());
		user.setClub(dto.club());

		user = userRepository.save(user);

		return UserResponse.builder()
			.user(user)
			.build();
	}

	public UserResponse updateClub(User user, UpdateClubRequest request) {

		user.setClub(request.club());

		user = userRepository.save(user);

		return UserResponse.builder()
			.user(user)
			.build();
	}
}
