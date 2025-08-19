package com.vidcentral.global.error.exception;

import com.vidcentral.global.error.model.ErrorCode;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {

	private final ErrorCode errorCode;

	public ForbiddenException(ErrorCode errorCode, Object... args) {
		super(errorCode.getFormattedMessage(args));
		this.errorCode = errorCode;
	}
}
