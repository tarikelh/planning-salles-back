package fr.dawan.calendarproject.dto;

import java.time.LocalDate;

import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.enums.InterventionStatus;

public class InterventionDto {

	private long id;
	
	private String planningComment;
	
	private Location location;
	
	private CourseDto course;
	
	private AvancedUserDto user;
	
	private InterventionStatus status; 
	
	private boolean optionStatus; //change to confirmStatus name

	private LocalDate dateStart;

	private LocalDate dateEnd;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getDateStart() {
		return dateStart;
	}

	public void setDateStart(LocalDate dateStart) {
		this.dateStart = dateStart;
	}

	public LocalDate getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(LocalDate dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getPlanningComment() {
		return planningComment;
	}

	public void setPlanningComment(String planningComment) {
		this.planningComment = planningComment;
	}

	public AvancedUserDto getUser() {
		return user;
	}

	public void setUser(AvancedUserDto user) {
		this.user = user;
	}

	public CourseDto getCourse() {
		return course;
	}

	public void setCourse(CourseDto course) {
		this.course = course;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public InterventionStatus getStatus() {
		return status;
	}

	public void setStatus(InterventionStatus status) {
		this.status = status;
	}

	public boolean isOptionStatus() {
		return optionStatus;
	}

	public void setOptionStatus(boolean optionStatus) {
		this.optionStatus = optionStatus;
	}
}
