package fr.dawan.calendarproject.dto;

public class InterventionFollowedDto {

	private long id;

	private long studentId;
	
	private long interventionId;
	
	private String registrationSlug;
	
	private int version;

	
	public InterventionFollowedDto(long id, long studentId, long interventionId, String registrationSlug,
			int version) {
		super();
		this.id = id;
		this.studentId = studentId;
		this.interventionId = interventionId;
		this.registrationSlug = registrationSlug;
		this.version = version;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public long getInterventionId() {
		return interventionId;
	}

	public void setInterventionId(long interventionId) {
		this.interventionId = interventionId;
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
