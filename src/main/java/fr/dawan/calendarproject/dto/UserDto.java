package fr.dawan.calendarproject.dto;

import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;

public class UserDto {

	private long id;

	private String firstName;
	
	private String lastName;

	private LocationDto location;

	private String email;
	
	private String password;

	private UserType type;
	
	private UserCompany company;
	
	private String imagePath;

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
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocationDto getLocation() {
		return location;
	}

	public void setLocation(LocationDto location) {
		this.location = location;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public UserCompany getCompany() {
		return company;
	}

	public void setCompany(UserCompany company) {
		this.company = company;
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
