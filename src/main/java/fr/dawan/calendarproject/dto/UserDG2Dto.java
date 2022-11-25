package fr.dawan.calendarproject.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDG2Dto {

	@JsonProperty("id")
	private long employeeId;

	private long personId;

	private String firstName;

	private String lastName;

	private long locationId;

	private String email;

	@JsonProperty("job")
	private String type;

	@JsonProperty("name")
	private String company;

	@JsonProperty("skill")
	@JsonAlias("skills")
	private String skills;

	private String endDate;

	private int version;

	public UserDG2Dto() {
	}

	public UserDG2Dto(long employeeId, long personId, String firstName, String lastName, long locationId, String email,
			String type, String company, String skills, String endDate, int version) {
		this.employeeId = employeeId;
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.locationId = locationId;
		this.email = email;
		this.type = type;
		this.company = company;
		this.skills = skills;
		this.endDate = endDate;
		this.version = version;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public long getPersonId() {
		return personId;
	}

	public void setPersonId(long personId) {
		this.personId = personId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}