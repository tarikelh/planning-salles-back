package fr.dawan.calendarproject.dto;

public class UserDto {

	private long id;
	
	private long idDg2;

	private String firstName;
	
	private String lastName;

	private long locationId;

	private String email;
	
	private String password;

	private String type;
	
	private String company;
	
	private String imagePath;
	
	private int version;

	public UserDto() {
	}

	public UserDto(long id, long idDg2, String firstName, String lastName, long locationId, String email,
			String password, String type, String company, String imagePath, int version) {
		super();
		this.id = id;
		this.idDg2 = idDg2;
		this.firstName = firstName;
		this.lastName = lastName;
		this.locationId = locationId;
		this.email = email;
		this.password = password;
		this.type = type;
		this.company = company;
		this.imagePath = imagePath;
		this.version = version;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdDg2() {
		return idDg2;
	}

	public void setIdDg2(long idDg2) {
		this.idDg2 = idDg2;
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

	public String getFullName() {
		return firstName + " "+ lastName;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
