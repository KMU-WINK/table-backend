package com.github.kmu_wink.domain.reservation.util.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ReservationTimeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ReservationTime {

	String message() default "올바른 예약 시간을 입력해주세요.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}