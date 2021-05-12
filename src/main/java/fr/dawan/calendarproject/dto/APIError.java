package fr.dawan.calendarproject.dto;

import org.springframework.http.HttpStatus;

public class APIError {

	private HttpStatus status;
	private String message;

	public APIError(HttpStatus internalServerError, String message) {
		this.status = internalServerError;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}