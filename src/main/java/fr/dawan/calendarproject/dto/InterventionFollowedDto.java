package fr.dawan.calendarproject.dto;

public class InterventionFollowedDto {

	private long id;

	private UserDto userDto;
	
	private InterventionDto interventionDto;
	
	private String registrationSlug;
	
	private int version;

	public InterventionFollowedDto(long id, UserDto userDto, InterventionDto interventionDto, String registrationSlug,
			int version) {
		super();
		this.id = id;
		this.userDto = userDto;
		this.interventionDto = interventionDto;
		this.registrationSlug = registrationSlug;
		this.version = version;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

	public InterventionDto getInterventionDto() {
		return interventionDto;
	}

	public void setInterventionDto(InterventionDto interventionDto) {
		this.interventionDto = interventionDto;
	}

	public String getRegistrationSlug() {
		return registrationSlug;
	}

	public void setRegistrationSlug(String registrationSlug) {
		this.registrationSlug = registrationSlug;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	
}
