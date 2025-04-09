package com.github.kmu_wink.domain.user.exception;

import com.github.kmu_wink.common.api.exception.ApiException;

public class UserException extends ApiException {

	private UserException(UserExceptions userExceptions) {

		super(userExceptions.getMessage());
	}

	public static UserException of(UserExceptions userExceptions) {

		return new UserException(userExceptions);
	}
}
