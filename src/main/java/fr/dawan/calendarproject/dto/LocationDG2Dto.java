package fr.dawan.calendarproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDG2Dto {

	private long id;
	private String name;
	private boolean published;
	private String country;
	private String colorCode;

	public LocationDG2Dto() {
	}

	public LocationDG2Dto(long id, String name, boolean published, String country, String colorCode) {
		this.id = id;
		this.name = name;
		this.published = published;
		this.country = country;
		this.colorCode = colorCode;
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

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
}
