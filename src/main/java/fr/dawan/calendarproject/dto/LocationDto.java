package fr.dawan.calendarproject.dto;

public class LocationDto {
	private long id;

	private String city;

	private String color;
	
	private int version;

	public LocationDto() {
	}

	public LocationDto(long id, String city, String color, int version) {
		this.id = id;
		this.city = city;
		this.color = color;
		this.version = version;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
