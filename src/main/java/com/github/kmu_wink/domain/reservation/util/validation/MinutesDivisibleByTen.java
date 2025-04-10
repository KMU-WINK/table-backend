package com.github.kmu_wink.domain.reservation.util.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = MinutesDivisibleByTenValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MinutesDivisibleByTen {

	String message() default "10분 단위로 입력해주세요.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}