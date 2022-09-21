package fr.dawan.calendarproject.dto;

import java.io.Serializable;
import java.time.LocalDate;

@SuppressWarnings("serial")
public class TaskDg2Dto implements Serializable{

	
	private long taskId;
	
	private long employeeId;
	
	private long personId;
	
	private String registrationSlug;
	
	private LocalDate beginAt;
	
	private LocalDate finishAt;
	
	private long interventionId;
	
	

	public TaskDg2Dto() {
	}

	public TaskDg2Dto(long taskId, long employeeId, long personId, String registrationSlug, LocalDate beginAt,
			LocalDate finishAt, long interventionId) {
		super();
		this.taskId = taskId;
		this.employeeId = employeeId;
		this.personId = personId;
		this.registrationSlug = registrationSlug;
		this.beginAt = beginAt;
		this.finishAt = finishAt;
		this.interventionId = interventionId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
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

	public String getRegistrationSlug() {
		return registrationSlug;
	}

	public void setRegistrationSlug(String registrationSlug) {
		this.registrationSlug = registrationSlug;
	}

	public LocalDate getBeginAt() {
		return beginAt;
	}

	public void setBeginAt(LocalDate beginAt) {
		this.beginAt = beginAt;
	}

	public LocalDate getFinishAt() {
		return finishAt;
	}

	public void setFinishAt(LocalDate finishAt) {
		this.finishAt = finishAt;
	}

	public long getInterventionId() {
		return interventionId;
	}

	public void setInterventionId(long interventionId) {
		this.interventionId = interventionId;
	}
	

	
	
	
}
