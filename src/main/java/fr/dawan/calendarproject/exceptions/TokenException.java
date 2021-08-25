package fr.dawan.calendarproject.exceptions;

import fr.dawan.calendarproject.dto.APIError;

public class TokenException extends RuntimeException {

	private APIError errorMessage;
	private static final long serialVersionUID = 1L;
	
	public TokenException(APIError errorMessage) {
		setErrorMessage(errorMessage);
	}

	public APIError getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(APIError errorMessage) {
		this.errorMessage = errorMessage;
	}
}
