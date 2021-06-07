package fr.dawan.calendarproject.validators;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

import fr.dawan.calendarproject.annotations.DatesSequenceValidation;

public class DatesSequenceValidator implements ConstraintValidator<DatesSequenceValidation, Object> {

	private String startField;
	private String endField;

	@Override
	public void initialize(DatesSequenceValidation constraint) {
		startField = constraint.startField();
		endField = constraint.endField();
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		try {
			LocalDate startFieldValue = getFieldValue(object, startField);
			LocalDate endFieldValue = getFieldValue(object, endField);
			return startFieldValue.isBefore(endFieldValue);
		} catch (Exception e) {
			return false;
		}
	}

	private LocalDate getFieldValue(Object object, String fieldName) throws Exception {
		Class<?> clazz = object.getClass();
		Field dateTime = clazz.getDeclaredField(fieldName);
		dateTime.setAccessible(true);
		Object result = dateTime.get(object);
		return (LocalDate)result;
	}
}
