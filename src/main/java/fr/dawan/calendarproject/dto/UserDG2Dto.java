package fr.dawan.calendarproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDG2Dto {

	private long id;

	private String FirstName;

	private String LastName;

	private long locationId;

	private String email;

	private String password;

	@JsonProperty("job")
	private String type;

	@JsonProperty("name")
	private String company;

	private String imagePath;

	@JsonProperty("skill")
	private String skills;

	private int version;

	public UserDG2Dto() {
	}

	public UserDG2Dto(long id, String firstName, String lastName, long locationId, String email, String password,
			String type, String company, String imagePath, String skills, int version) {
		setId(id);
		setFirstName(firstName);
		setLastName(lastName);
		setLocationId(locationId);
		setEmail(email);
		setPassword(password);
		setType(type);
		setCompany(company);
		setImagePath(imagePath);
		setSkills(skills);
		setVersion(version);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public long getLocationId() {
		return locationId;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
