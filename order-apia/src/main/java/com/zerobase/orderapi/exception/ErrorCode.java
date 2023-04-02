package com.zerobase.orderapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	EXIST(HttpStatus.BAD_REQUEST, "불가");
	private final HttpStatus httpStatus;
	private final String detail;
}
