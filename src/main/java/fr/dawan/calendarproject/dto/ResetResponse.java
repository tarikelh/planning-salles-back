package fr.dawan.calendarproject.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement(name = "login-reponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResetResponse implements Serializable {

	private String token;
	private String password;
	
	public ResetResponse(String token, String password) {
		this.token = token;
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResetResponse [token=").append(token).append(", password=").append(password).append("]");
		return builder.toString();
	}

	
}
