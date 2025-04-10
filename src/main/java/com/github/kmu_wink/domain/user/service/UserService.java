package com.github.kmu_wink.domain.user.service;

import static com.github.kmu_wink.domain.user.exception.UserExceptions.*;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.github.kmu_wink.domain.user.dto.request.SignUpRequest;
import com.github.kmu_wink.domain.user.dto.request.UpdateClubRequest;
import com.github.kmu_wink.domain.user.dto.response.UserResponse;
import com.github.kmu_wink.domain.user.dto.response.UsersResponse;
import com.github.kmu_wink.domain.user.exception.UserException;
import com.github.kmu_wink.domain.user.repository.UserRepository;
import com.github.kmu_wink.domain.user.schema.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public UsersResponse getAllUser(String query) {

		List<User> users = userRepository.findAllByNameContaining(query);

		return UsersResponse.builder()
			.users(users)
			.build();
	}

	public UserResponse getMyInfo(User user) {

		return UserResponse.builder()
			.user(user)
			.build();
	}

	public UserResponse signUp(User user, SignUpRequest dto) {

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
