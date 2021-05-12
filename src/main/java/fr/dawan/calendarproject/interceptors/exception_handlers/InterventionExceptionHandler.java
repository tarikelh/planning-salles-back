package fr.dawan.calendarproject.interceptors.exception_handlers;

import java.io.PrintWriter;
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

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.exceptions.BadInterventionFormatException;

@ControllerAdvice
public class InterventionExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { BadInterventionFormatException.class })
	protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		pw.close();
		Logger.getAnonymousLogger().log(Level.SEVERE, sw.toString());
		APIError myError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());

		return handleExceptionInternal(ex, myError, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
