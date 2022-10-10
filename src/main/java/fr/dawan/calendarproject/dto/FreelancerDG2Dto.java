package fr.dawan.calendarproject.dto;

public class FreelancerDG2Dto {
	private long id;
	private long personId;
	private String firstName;
	private String lastName;
	private long locationId;
	private String email;
	private String name;
	private String skills;
	private String slug;

	public FreelancerDG2Dto() {
	}

	public FreelancerDG2Dto(long id, long personId, String firstName, String lastName, long locationId, String email,
			String name, String skills, String slug) {
		this.id = id;
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.locationId = locationId;
		this.email = email;
		this.name = name;
		this.skills = skills;
		this.slug = slug;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}
}
