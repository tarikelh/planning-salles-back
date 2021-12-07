package fr.dawan.calendarproject.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Location {
	@Id
	@Column(unique = true)
	private long id;

	@Column(nullable = false, length = 255, unique = true)
	private String city;

	@Column(nullable = true, length = 9)
	private String color;

	@Version
	private int version;

	public Location() {
	}

	public Location(long id, String city, String color, int version) {
		setId(id);
		setCity(city);
		setColor(color);
		setVersion(version);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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

	@Override
	public String toString() {
		return "Location [id=" + getId() + ", city=" + getCity() + ", color=" + getColor() + "]";
	}

}
