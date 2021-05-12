package fr.dawan.calendarproject.dto;

public class APIError {

	private int errorCode;
	private String instanceClass;
	private String type;
	private String message;

	public APIError(int errorCode, String instanceClass, String type, String message) {
		setErrorCode(errorCode);
		setInstanceClass(instanceClass);
		setType(type);
		setMessage(message);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInstanceClass() {
		return instanceClass;
	}

	public void setInstanceClass(String instanceClass) {
		this.instanceClass = instanceClass;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}