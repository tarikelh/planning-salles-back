package fr.dawan.calendarproject.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement(name = "login")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginDto implements Serializable {

	private String email;
	private String password;

	public LoginDto(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public LoginDto() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
