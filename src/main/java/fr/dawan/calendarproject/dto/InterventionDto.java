package fr.dawan.calendarproject.dto;

import java.time.LocalDate;

import fr.dawan.calendarproject.entities.Location;

public class InterventionDto {

	private long id;

	private LocalDate dateStart;

	private LocalDate dateEnd;

	private String planningComment;

	private AvancedUserDto user;

	private CourseDto course;

	private Location location;

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
}
