package fr.dawan.calendarproject.dto;

public class LocationDto {
	private long id;
	
	private long idDg2;

	private String city;

	private String countryCode;
	
	private String color;
	
	private boolean published;
	
	private int version;

	public LocationDto() {
	}

	public LocationDto(long id, long idDg2, String city, String contryCode, String color, boolean published, int version) {
		super();
		this.id = id;
		this.idDg2 = idDg2;
		this.city = city;
		this.countryCode = contryCode;
		this.color = color;
		this.published = published;
		this.version = version;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getIdDg2() {
		return idDg2;
	}

	public void setIdDg2(long idDg2) {
		this.idDg2 = idDg2;
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
	
	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String contryCode) {
		this.countryCode = contryCode;
	}

}
