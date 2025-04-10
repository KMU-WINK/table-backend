package com.github.kmu_wink.common.api.exception;

import java.util.Objects;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.github.kmu_wink.common.api.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({NoResourceFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ApiResponse<?> noResourceFoundException(Exception ignored) {

        return ApiResponse.error("요청하신 리소스를 찾을 수 없습니다.");
    }

    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        MissingServletRequestParameterException.class,
        MissingServletRequestPartException.class
    })
    public ApiResponse<?> httpMessageNotReadableException(Exception ignored) {

        return ApiResponse.error("요청 데이터가 올바르지 않습니다.");
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ApiResponse<?> authorizationDeniedException(AuthorizationDeniedException ignored) {

        return ApiResponse.error("권한이 없습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {

        if (Objects.nonNull(e.getBindingResult().getFieldError())) {

            String field = e.getBindingResult().getFieldError().getField();
            String message = e.getBindingResult().getFieldError().getDefaultMessage();
            String errorMessage = String.format("%s은(는) %s", field, message);

            return ApiResponse.error(errorMessage);
        }

        if (Objects.nonNull(e.getBindingResult().getGlobalError())) {

            String errorMessage = e.getBindingResult().getGlobalError().getDefaultMessage();

            return ApiResponse.error(errorMessage);
        }

        System.out.println(e.getBindingResult().getGlobalError());

        return ApiResponse.error(e.getMessage());
    }

    @ExceptionHandler(ApiException.class)
    public ApiResponse<?> apiException(ApiException e) {

        return ApiResponse.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> exception(Exception e) {

        log.error("", e);

        return ApiResponse.error("알 수 없는 오류가 발생했습니다.");
    }
}
