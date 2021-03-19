package fr.dawan.calendarproject.dto;

import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.enums.UserCompanie;
import fr.dawan.calendarproject.enums.UserType;

public class UserDto {

	private long id;

	private String firstName;

	private Location location;

	private String mail;

	private UserType type;
	
	private UserCompanie companie;
	
	private String imagePath;
	
	private String password;

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

	public UserCompanie getCompanie() {
		return companie;
	}

	public void setCompanie(UserCompanie companie) {
		this.companie = companie;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
