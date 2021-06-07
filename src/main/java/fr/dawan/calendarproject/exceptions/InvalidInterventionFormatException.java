package fr.dawan.calendarproject.exceptions;

import java.util.Set;

import fr.dawan.calendarproject.dto.APIError;

public class InvalidInterventionFormatException extends RuntimeException {

	private Set<APIError> errors;
	private static final long serialVersionUID = 1L;

	public InvalidInterventionFormatException(Set<APIError> errors) {
		setErrors(errors);
	}

	public Set<APIError> getErrors() {
		return errors;
	}

	public void setErrors(Set<APIError> errors) {
		this.errors = errors;
	}

}
