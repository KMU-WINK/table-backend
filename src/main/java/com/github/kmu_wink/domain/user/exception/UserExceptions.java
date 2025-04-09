package com.github.kmu_wink.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExceptions {

	ALREADY_SIGNUP("이미 가입된 유저입니다.")
	;

	private final String message;
}
