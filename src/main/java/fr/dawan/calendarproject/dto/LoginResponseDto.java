package fr.dawan.calendarproject.dto;

public class LoginResponseDto {
	
	private long id;
	private String name;
	private String token;

	public LoginResponseDto() {
		
	}
	
	public LoginResponseDto(long id, String name, String token) {
		this.id = id;
		this.name = name;
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
