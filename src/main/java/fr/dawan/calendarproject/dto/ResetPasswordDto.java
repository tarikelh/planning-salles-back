package fr.dawan.calendarproject.dto;

public class ResetPasswordDto {
	private String email;
	private String password;
	
	public ResetPasswordDto(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
	public ResetPasswordDto() {
		super();
	}
	public ResetPasswordDto(String email) {
		this.email = email;
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
