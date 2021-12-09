package fr.dawan.calendarproject.dto;

import java.io.Serializable;

public class APIError implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2683647596234001146L;
	private int errorCode;
	private String instanceClass;
	private String type;
	private String message;
	private String path;

	public APIError(int errorCode, String instanceClass, String type, String message, String path) {
		setErrorCode(errorCode);
		setInstanceClass(instanceClass);
		setType(type);
		setMessage(message);
		setPath(path);
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}