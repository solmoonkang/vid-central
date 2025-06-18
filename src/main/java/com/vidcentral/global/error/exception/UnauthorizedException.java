package com.vidcentral.global.error.exception;

import com.vidcentral.global.error.model.ErrorMessage;

public class UnauthorizedException extends VIDCentralException {

	public UnauthorizedException(ErrorMessage errorMessage) {
		super(errorMessage);
	}
}
