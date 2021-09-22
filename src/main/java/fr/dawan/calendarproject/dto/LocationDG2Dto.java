package fr.dawan.calendarproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class LocationDG2Dto {
	
	private String slug;
	private String name;
	private String address;
	private float latitude;
	private float longitude;
	private String zipCode;
	private String city;
	private String country;
	private String furtherInfo;
	private String mapUrl;
	private String pictureFile;
	private boolean office;
	
	public LocationDG2Dto() {
	
	}

	public LocationDG2Dto(String slug, String name, String address, float latitude, float longitude, String zipCode,
			String city, String country, String furtherInfo, String mapUrl, String pictureFile, boolean office) {
		this.slug = slug;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.zipCode = zipCode;
		this.city = city;
		this.country = country;
		this.furtherInfo = furtherInfo;
		this.mapUrl = mapUrl;
		this.pictureFile = pictureFile;
		this.office = office;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFurtherInfo() {
		return furtherInfo;
	}

	public void setFurtherInfo(String furtherInfo) {
		this.furtherInfo = furtherInfo;
	}

	public String getMapUrl() {
		return mapUrl;
	}

	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl;
	}

	public String getPictureFile() {
		return pictureFile;
	}

	public void setPictureFile(String pictureFile) {
		this.pictureFile = pictureFile;
	}

	public boolean isOffice() {
		return office;
	}

	public void setOffice(boolean office) {
		this.office = office;
	}
}
