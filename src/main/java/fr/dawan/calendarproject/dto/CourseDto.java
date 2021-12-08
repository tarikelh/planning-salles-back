package fr.dawan.calendarproject.dto;

public class CourseDto {

	private long id;

	private String title;

	private String duration;

	private String slug;

	private int version;

	public CourseDto() {
	}

	public CourseDto(long id, String title, String duration, String slug, int version) {
		this.id = id;
		this.title = title;
		this.duration = duration;
		this.slug = slug;
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

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
