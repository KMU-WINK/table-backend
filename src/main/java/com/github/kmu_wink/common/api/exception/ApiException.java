package com.github.kmu_wink.common.api.exception;

public abstract class ApiException extends RuntimeException {

    protected ApiException(String message) {

        super(message);
    }
}