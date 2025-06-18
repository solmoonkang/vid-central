package com.vidcentral.global.error.handler;

import static com.vidcentral.global.error.model.ErrorMessage.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vidcentral.global.error.exception.BadRequestException;
import com.vidcentral.global.error.exception.ConflictException;
import com.vidcentral.global.error.exception.ForbiddenException;
import com.vidcentral.global.error.exception.NotFoundException;
import com.vidcentral.global.error.exception.UnauthorizedException;
import com.vidcentral.global.error.exception.VIDCentralException;
import com.vidcentral.global.error.model.ErrorResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalExceptionHandler {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	protected ErrorResponse handleException() {
		log.error("[✅ LOGGER] UNKNOWN ERROR: 알 수 없는 에러");
		return new ErrorResponse(FAILED_UNKNOWN_ERROR.getMessage(), null);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(VIDCentralException.class)
	protected ErrorResponse handleAuthPlaygroundException(VIDCentralException vidCentralException) {
		log.error("[✅ LOGGER] SERVER ERROR: 서버 에러");
		return new ErrorResponse(vidCentralException.getMessage(), null);
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(ConflictException.class)
	protected ErrorResponse handleConflictException(VIDCentralException vidCentralException) {
		log.error("[✅ LOGGER] CONFLICT ERROR: 충돌 에러");
		return new ErrorResponse(vidCentralException.getMessage(), null);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	protected ErrorResponse handleNotFoundException(VIDCentralException vidCentralException) {
		log.error("[✅ LOGGER] NOT FOUND ERROR: 리소스를 찾을 수 없는 에러");
		return new ErrorResponse(vidCentralException.getMessage(), null);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestException.class)
	protected ErrorResponse handleBadRequestException(VIDCentralException vidCentralException) {
		log.error("[✅ LOGGER] BAD REQUEST ERROR: 잘못된 요청 에러");
		return new ErrorResponse(vidCentralException.getMessage(), null);
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedException.class)
	protected ErrorResponse handleUnauthorizedException(VIDCentralException vidCentralException) {
		log.error("[✅ LOGGER] UNAUTHORIZED ERROR: 인증되지 않은 사용자 에러");
		return new ErrorResponse(vidCentralException.getMessage(), null);
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(ForbiddenException.class)
	protected ErrorResponse handleForbiddenException(VIDCentralException vidCentralException) {
		log.error("[✅ LOGGER] FORBIDDEN ERROR: 허용되지 않은 권한 에러");
		return new ErrorResponse(vidCentralException.getMessage(), null);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponse handleException(MethodArgumentNotValidException methodArgumentNotValidException) {
		log.error("[✅ LOGGER] METHOD ARGUMENT NOT VALID ERROR: 잘못된 요청 에러");

		Map<String, String> validationMap = new HashMap<>();

		methodArgumentNotValidException.getBindingResult()
			.getFieldErrors()
			.forEach(fieldError -> validationMap.put(fieldError.getField(), fieldError.getDefaultMessage()));

		return new ErrorResponse(FAILED_INVALID_REQUEST_ERROR.getMessage(), validationMap);
	}
}
