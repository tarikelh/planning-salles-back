package fr.dawan.calendarproject.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement(name = "login-reponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResetResponseDto implements Serializable {

	private String email;

	public ResetResponseDto() {
	}

	public ResetResponseDto(String email) {
		this.email = email;
	}

	public String getName() {
		return email;
	}

	public void setName(String email) {
		this.email = email;
	}

}
