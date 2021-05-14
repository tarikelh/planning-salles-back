package fr.dawan.calendarproject.interceptors.exception_handlers;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import fr.dawan.calendarproject.exceptions.InvalidInterventionFormatException;

@ControllerAdvice
public class InterventionExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { InvalidInterventionFormatException.class })
	protected ResponseEntity<Object> handleConflict(InvalidInterventionFormatException ex, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();

		StringWriter sw = new StringWriter();

		Logger.getAnonymousLogger().log(Level.SEVERE, sw.toString());

		return handleExceptionInternal(ex, ex.getErrors(), headers, HttpStatus.BAD_REQUEST, request);
	}
}
