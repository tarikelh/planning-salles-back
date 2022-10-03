package fr.dawan.calendarproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InterventionFollowedDG2Dto {
	
	private long interventionId;
	private long personId;
	private long employeeId;
    private String registrationSlug;

    
	public InterventionFollowedDG2Dto() {
	}

	public InterventionFollowedDG2Dto(long interventionId, long personId, long employeeId, String registrationSlug) {
		this.interventionId = interventionId;
		this.personId = personId;
		this.employeeId = employeeId;
		this.registrationSlug = registrationSlug;
	}

	public long getInterventionId() {
		return interventionId;
	}

	public void setInterventionId(long interventionId) {
		this.interventionId = interventionId;
	}

	public long getPersonId() {
		return personId;
	}

	public void setPersonId(long personId) {
		this.personId = personId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getRegistrationSlug() {
		return registrationSlug;
	}

	public void setRegistrationSlug(String registrationSlug) {
		this.registrationSlug = registrationSlug;
	}
	   
}
