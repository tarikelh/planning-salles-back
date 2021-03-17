package fr.dawan.calendarproject.dto;

import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.UserType;

public class UserDto {

	private long id;

	private String firstName;

	private Location location;

	private String mail;

	private UserType type;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String name) {
		this.firstName = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}
}
