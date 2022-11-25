package fr.dawan.calendarproject.dto;

public class SyncPlanningDto {
	private String email;
	private String password;
	private String start;
	private String end;

	public SyncPlanningDto() {
	}

	public SyncPlanningDto(String email, String password, String start, String end) {
		setEmail(email);
		setPassword(password);
		setStart(start);
		setEnd(end);
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

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
}
