package com.gyp.common.exceptions;

import com.gyp.common.dtos.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(value = AccessDeniedException.class)
	ResponseEntity<ApiResponse> handlingAccessDenied(AccessDeniedException exception) {
		log.error("Exception: ", exception);
		ApiResponse apiResponse = new ApiResponse();

		apiResponse.setCode(HttpStatus.FORBIDDEN.value());
		apiResponse.setMessage(HttpStatus.FORBIDDEN.getReasonPhrase());

		return ResponseEntity.badRequest().body(apiResponse);
	}

	@ExceptionHandler(value = UnAuthorizationException.class)
	ResponseEntity<ApiResponse> handlingUnAuthorization(UnAuthorizationException exception) {
		log.error("Exception: ", exception);
		ApiResponse apiResponse = new ApiResponse();

		apiResponse.setCode(HttpStatus.UNAUTHORIZED.value());
		apiResponse.setMessage(HttpStatus.UNAUTHORIZED.getReasonPhrase());

		return ResponseEntity.badRequest().body(apiResponse);
	}

	@ExceptionHandler(value = ResourceNotFoundException.class)
	ResponseEntity<ApiResponse> handlingResourceNotFoundException(ResourceNotFoundException exception) {
		log.error("Exception: ", exception);
		ApiResponse apiResponse = new ApiResponse();

		apiResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		apiResponse.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

		return ResponseEntity.badRequest().body(apiResponse);
	}
}
