package fr.dawan.calendarproject.dto;

public class InterventionsFollowedDto {

	private long id;

	private long studentId;
	
	private long interventionId;

	public InterventionsFollowedDto(long id, long studentId, long interventionId) {
		super();
		this.id = id;
		this.studentId = studentId;
		this.interventionId = interventionId;
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

	
}
