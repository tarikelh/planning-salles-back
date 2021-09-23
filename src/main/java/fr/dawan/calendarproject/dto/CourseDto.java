package fr.dawan.calendarproject.dto;

public class CourseDto {

	private long id;

	private String title;
	
	private String duration;
	
	private int version;
	
	public CourseDto() {
	}
	
	public CourseDto(long id, String title, String duration, int version) {
		this.id = id;
		this.title = title;
		this.duration = duration;
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

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
