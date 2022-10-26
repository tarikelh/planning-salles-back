package fr.dawan.calendarproject.dto;

public class InterventionFollowedDto {

	private long id;

	private UserDto userDto;
	
	private AdvancedInterventionDto advInterventionDto;
	
	private String registrationSlug;
	
	private int version;

	

	public InterventionFollowedDto(long id, UserDto userDto, AdvancedInterventionDto advInterventionDto,
            String registrationSlug, int version) {
        super();
        this.id = id;
        this.userDto = userDto;
        this.advInterventionDto = advInterventionDto;
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

	public AdvancedInterventionDto getAdvInterventionDto() {
        return advInterventionDto;
    }

    public void setAdvInterventionDto(AdvancedInterventionDto advInterventionDto) {
        this.advInterventionDto = advInterventionDto;
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
	
	public long getUserId() {
	    long userId = 0;
	    if(this.userDto != null) {
	        userId = this.userDto.getId(); 
	    }
        return userId;
    }
	
	public long getInterventionId() {
	    long interventionId = 0;
        if(this.advInterventionDto != null) {
            interventionId = this.advInterventionDto.getId();
        }
        return interventionId;
    }

	
}
