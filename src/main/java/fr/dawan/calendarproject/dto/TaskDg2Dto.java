package fr.dawan.calendarproject.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TaskDg2Dto implements Serializable{

	
	private long taskId;
	
	private String title;
	
	private long employeeId;
	
	private long personId;
	
	private String taskSlug;
	
	private String beginAt;
	
	private String finishAt;
	
	private long interventionId;
	
	

	public TaskDg2Dto() {
	}

	public TaskDg2Dto(long taskId, String title, long employeeId, long personId, String taskSlug, String beginAt,
			String finishAt, long interventionId) {
		super();
		this.taskId = taskId;
		this.title = title;
		this.employeeId = employeeId;
		this.personId = personId;
		this.taskSlug = taskSlug;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getTaskSlug() {
		return taskSlug;
	}

	public void setTaskSlug(String taskSlug) {
		this.taskSlug = taskSlug;
	}


	public String getBeginAt() {
		return beginAt;
	}

	public void setBeginAt(String beginAt) {
		this.beginAt = beginAt;
	}

	public String getFinishAt() {
		return finishAt;
	}

	public void setFinishAt(String finishAt) {
		this.finishAt = finishAt;
	}

	public long getInterventionId() {
		return interventionId;
	}

	public void setInterventionId(long interventionId) {
		this.interventionId = interventionId;
	}
	

	
	
	
}
