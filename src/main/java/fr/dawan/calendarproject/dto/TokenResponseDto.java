package fr.dawan.calendarproject.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TokenResponseDto {
	private String value;
	private LocalDateTime expirationDate;

	public TokenResponseDto() {
	}

	public TokenResponseDto(String value, LocalDateTime expirationDate) {
		setValue(value);
		setExpirationDate(expirationDate);
	}
	
	public TokenResponseDto(String value, Date expirationDate) {
		setValue(value);
		setExpirationDate(expirationDate);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String token) {
		this.value = token;
	}

	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = LocalDateTime.ofInstant(expirationDate.toInstant(), ZoneId.systemDefault());
	}
}
