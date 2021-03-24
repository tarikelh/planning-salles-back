package fr.dawan.calendarproject.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 255)
	private String city;

	@Column(nullable = true, length = 9)
	private String color;

	@Version
	private int version;

	public Location() {
	}

	public Location(String city, String color) {
		setCity(city);
		setColor(color);
	}
	
	public Location(String city) {
		setCity(city);
	}
	
	//CHANGE FOR SETTER
	public Location(long id, String city, String color, int version) {
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
