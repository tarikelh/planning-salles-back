package fr.dawan.calendarproject.dto;

import java.util.ArrayList;
import java.util.List;

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

	private List<Long> skillsId;

	private int version;

	public UserDG2Dto() {
		setSkillsId(new ArrayList<Long>());
	}

	public UserDG2Dto(long id, String firstName, String lastName, long locationId, String email, String password,
			String type, String company, String imagePath, List<Long> skillsId, int version) {
		setId(id);
		setFirstName(firstName);
		setLastName(lastName);
		setLocationId(locationId);
		setEmail(email);
		setPassword(password);
		setType(type);
		setCompany(company);
		setImagePath(imagePath);
		setSkillsId(skillsId);
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

	public List<Long> getSkillsId() {
		return skillsId;
	}

	public void setSkillsId(List<Long> skillsId) {
		this.skillsId = skillsId;
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
