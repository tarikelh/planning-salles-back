package fr.dawan.calendarproject.dto;

public class LoginResponseDto {

	private UserDto user;
	private TokenResponseDto token;

	public LoginResponseDto() {
	}

	public LoginResponseDto(UserDto user, TokenResponseDto token) {
		this.user = user;
		this.token = token;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public TokenResponseDto getToken() {
		return token;
	}

	public void setToken(TokenResponseDto token) {
		this.token = token;
	}
}
