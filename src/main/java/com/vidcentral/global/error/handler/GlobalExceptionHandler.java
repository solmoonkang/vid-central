package com.vidcentral.global.error.handler;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vidcentral.global.error.exception.BadRequestException;
import com.vidcentral.global.error.exception.ConflictException;
import com.vidcentral.global.error.exception.ForbiddenException;
import com.vidcentral.global.error.exception.InternalServerException;
import com.vidcentral.global.error.exception.NotFoundException;
import com.vidcentral.global.error.exception.UnauthorizedException;
import com.vidcentral.global.error.model.ErrorResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	protected ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException badRequestException) {
		log.error("[✅ LOGGER] BAD REQUEST ERROR: 잘못된 요청 에러, {}", badRequestException.getMessage());
		return ResponseEntity.badRequest()
			.body(new ErrorResponse(badRequestException.getMessage(), null));
	}

	@ExceptionHandler(UnauthorizedException.class)
	protected ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException unauthorizedException) {
		log.error("[✅ LOGGER] UNAUTHORIZED ERROR: 인증되지 않은 사용자 에러, {}", unauthorizedException.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(new ErrorResponse(unauthorizedException.getMessage(), null));
	}

	@ExceptionHandler(ForbiddenException.class)
	protected ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException forbiddenException) {
		log.error("[✅ LOGGER] FORBIDDEN ERROR: 허용되지 않은 권한 에러, {}", forbiddenException.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
			.body(new ErrorResponse(forbiddenException.getMessage(), null));
	}

	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException notFoundException) {
		log.error("[✅ LOGGER] NOT FOUND ERROR: 리소스를 찾을 수 없는 에러, {}", notFoundException.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new ErrorResponse(notFoundException.getMessage(), null));
	}

	@ExceptionHandler(ConflictException.class)
	protected ResponseEntity<ErrorResponse> handleConflictException(ConflictException conflictException) {
		log.error("[✅ LOGGER] CONFLICT ERROR: 충돌 에러, {}", conflictException.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(new ErrorResponse(conflictException.getMessage(), null));
	}

	@ExceptionHandler(InternalServerException.class)
	protected ResponseEntity<ErrorResponse> handleInternalServerException(
		InternalServerException internalServerException) {
		log.error("[✅ LOGGER] SERVER ERROR: 서버 에러, {}", internalServerException.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(internalServerException.getMessage(), null));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException methodArgumentNotValidException) {
		log.error("[✅ LOGGER] METHOD ARGUMENT NOT VALID ERROR: 잘못된 요청 에러");

		Map<String, String> validationMap = methodArgumentNotValidException.getBindingResult()
			.getFieldErrors()
			.stream()
			.collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));

		return ResponseEntity.badRequest().body(new ErrorResponse(
			"잘못된 요청입니다. 입력한 데이터가 유효하지 않습니다.", validationMap
		));
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception exception) {
		log.error("[✅ LOGGER] UNKNOWN ERROR: 알 수 없는 에러", exception);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse("서버에서 알 수 없는 에러가 발생했습니다.", null));
	}
}
