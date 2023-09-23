package com.zerobase.orderapi.exception;

import com.zerobase.orderapi.exception.CustomException.CustomExceptionResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionAdvice {

	@ExceptionHandler({CustomException.class})
	public ResponseEntity<CustomException.CustomExceptionResponse> exceptionHandler(
		HttpServletRequest request, final CustomException exception) {

		return ResponseEntity
			.status(exception.getStatus())
			.body(CustomExceptionResponse.builder()
				.message(exception.getMessage())
				.code(exception.getErrorCode().name())
				.status(exception.getStatus())
				.build());
	}
}
