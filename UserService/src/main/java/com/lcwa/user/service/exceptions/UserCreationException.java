package com.lcwa.user.service.exceptions;

import java.util.List;

public class UserCreationException extends CustomException {

	public UserCreationException(String errorCode, String message) {
		super(errorCode, message);
	}

	public UserCreationException(String errorCode, Throwable throwable) {
		super(errorCode, throwable);
	}

	public UserCreationException(String errorCode, String message, Throwable throwable) {
		super(errorCode, message, throwable);
	}

	public UserCreationException(UserCreationException e) {
		super(e);
	}
}
