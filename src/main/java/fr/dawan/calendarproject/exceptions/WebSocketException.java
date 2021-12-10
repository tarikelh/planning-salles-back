package fr.dawan.calendarproject.exceptions;

public class WebSocketException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public WebSocketException(String errorMessage) {
		super(errorMessage);
	}

}
