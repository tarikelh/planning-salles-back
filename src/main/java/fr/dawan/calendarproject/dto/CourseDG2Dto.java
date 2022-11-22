package fr.dawan.calendarproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseDG2Dto {

	private String title;
	private String duration;
	private String slug;
	private long id;

	public CourseDG2Dto() {

	}

	public CourseDG2Dto(String title, String duration, String slug, long id) {
		this.title = title;
		this.duration = duration;
		this.slug = slug;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}