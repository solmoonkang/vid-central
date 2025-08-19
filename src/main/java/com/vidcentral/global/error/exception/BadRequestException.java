package com.vidcentral.global.error.exception;

import com.vidcentral.global.error.model.ErrorCode;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

	private final ErrorCode errorCode;

	public BadRequestException(ErrorCode errorCode, Object... args) {
		super(errorCode.getFormattedMessage(args));
		this.errorCode = errorCode;
	}
}
