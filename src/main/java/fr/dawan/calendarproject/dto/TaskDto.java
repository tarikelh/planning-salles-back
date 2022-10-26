package fr.dawan.calendarproject.dto;

import java.io.Serializable;
import java.time.LocalDate;


@SuppressWarnings("serial")
public class TaskDto implements Serializable {
	
	
	private long id;
	
	private long taskIdDg2;
	
	private String title;
	
	private AdvancedInterventionDto2 intervention ;
	
	private UserDto user;
	
	private String slug;
	
	private LocalDate beginDate;
	
	private LocalDate endDate;
	
	private double duration;
	
	private int version;
	
	
	public TaskDto() {
		
	}
 

	public TaskDto(long id, long taskIdDg2, String title, AdvancedInterventionDto2 intervention, UserDto user,
			String slug, LocalDate beginDate, LocalDate endDate, double duration, int version) {
		super();
		this.id = id;
		this.taskIdDg2 = taskIdDg2;
		this.title = title;
		this.intervention = intervention;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public AdvancedInterventionDto2 getIntervention() {
		return intervention;
	}

	public void setIntervention(AdvancedInterventionDto2 intervention) {
		this.intervention = intervention;
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
	
	public long getUserId() {
		long result = 0 ;
		if(this.user!=null)
			result = this.user.getId();
		return result;
	}
	
	public long getInterventionId() {
		long result = 0;
		if(this.intervention!=null)
			result =this.intervention.getId();
		return result;
	}
	

}
