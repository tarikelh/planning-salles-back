package fr.dawan.calendarproject.dto;

import java.io.Serializable;
import java.time.LocalDate;


@SuppressWarnings("serial")
public class TaskDto implements Serializable {
	
	
	private long id;
	
	private long taskIdDg2;
	
	private long interventionId;
	
	private UserDto user;
	
	private String slug;
	
	private LocalDate beginDate;
	
	private LocalDate endDate;
	
	private double duration;
	
	private int version;
	
	
	public TaskDto() {
		
	}
 
	
	
	public TaskDto(long id, long taskIdDg2, long interventionId, UserDto user,
			String slug, LocalDate beginDate, LocalDate endDate, double duration, int version) {
		super();
		this.id = id;
		this.taskIdDg2 = taskIdDg2;
		this.interventionId = interventionId;
		this.user = user;
		this.slug = slug;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.duration = duration;
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
	
	
	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}
	
}
