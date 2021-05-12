package fr.dawan.calendarproject.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import fr.dawan.calendarproject.validators.DatesSequenceValidator;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DatesSequenceValidator.class})
public @interface DatesSequenceValidation {
	String message() default "Starting date must be before ending";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
	String startField();
	String endField();
}
