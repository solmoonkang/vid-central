package com.vidcentral.global.error.exception;

import com.vidcentral.global.error.model.ErrorCode;

import lombok.Getter;

@Getter
public class InternalServerException extends RuntimeException {

	private final ErrorCode errorCode;

	public InternalServerException(ErrorCode errorCode, Object... args) {
		super(errorCode.getFormattedMessage(args));
		this.errorCode = errorCode;
	}
}
