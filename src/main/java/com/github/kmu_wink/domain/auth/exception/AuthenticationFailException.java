package com.github.kmu_wink.domain.auth.exception;

import com.github.kmu_wink.common.api.ApiException;
import org.springframework.http.HttpStatus;

public class AuthenticationFailException extends ApiException {

    public AuthenticationFailException() {

        super(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다.");
    }
}
