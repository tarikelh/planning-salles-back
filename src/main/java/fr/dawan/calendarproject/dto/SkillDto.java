package fr.dawan.calendarproject.dto;

public class SkillDto {
	private long id;

	private String title;

	private int version;
	
	public SkillDto() {
	}

	public SkillDto(long id, String title, int version) {
		this.id = id;
		this.title = title;
		this.version = version;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
