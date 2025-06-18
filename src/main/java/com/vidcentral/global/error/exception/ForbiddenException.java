package com.vidcentral.global.error.exception;

import com.vidcentral.global.error.model.ErrorMessage;

public class ForbiddenException extends VIDCentralException {

	public ForbiddenException(ErrorMessage errorMessage) {
		super(errorMessage);
	}
}
