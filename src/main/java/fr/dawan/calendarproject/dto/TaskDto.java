package fr.dawan.calendarproject.dto;

import java.io.Serializable;
import java.time.LocalDate;

@SuppressWarnings("serial")
public class TaskDto implements Serializable {
	
	
	private long id;
	
	private long taskIdDg2;
	
	private long userId;
	
	private long interventionId;
	
	private String slug;
	
	private LocalDate beginDate;
	
	private LocalDate endDate;
	
	private int version;
	
	
	public TaskDto() {
		
	}

	public TaskDto(long id, long taskIdDg2, long userId, long interventionId, String slug, LocalDate beginDate, LocalDate endDate,
			int version) {
		super();
		this.id = id;
		this.taskIdDg2 = taskIdDg2;
		this.userId = userId;
		this.interventionId = interventionId;
		this.slug = slug;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.version = version;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getTaskIdDg2() {
		return taskIdDg2;
	}

	public void setTaskIdDg2(long taskIdDg2) {
		this.taskIdDg2 = taskIdDg2;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getInterventionId() {
		return interventionId;
	}

	public void setInterventionId(long interventionId) {
		this.interventionId = interventionId;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public LocalDate getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(LocalDate beginDate) {
		this.beginDate = beginDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
