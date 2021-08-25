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

import fr.dawan.calendarproject.exceptions.EntityFormatException;

@ControllerAdvice
public class EntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { EntityFormatException.class })
	protected ResponseEntity<Object> handleConflict(EntityFormatException ex, WebRequest request) {
		System.out.println("coucou " + request);

		HttpHeaders headers = new HttpHeaders();
		StringWriter sw = new StringWriter();

		ex.getErrors().forEach(e -> {
			sw.append("Type :" + e.getType());
			sw.append(" :: :: ");
			sw.append("Message :" + e.getMessage());
			sw.append("\r\n");
			
		});

		Logger.getAnonymousLogger().log(Level.SEVERE, sw.toString());
		

		return handleExceptionInternal(ex, ex.getErrors(), headers, HttpStatus.BAD_REQUEST, request);
	}
}
