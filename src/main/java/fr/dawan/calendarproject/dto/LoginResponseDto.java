package fr.dawan.calendarproject.dto;

public class LoginResponseDto {

	private UserDto user;
	private String token;

	public LoginResponseDto() {

	}

	public LoginResponseDto(UserDto user, String token) {
		this.user = user;
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}
}
