package fr.dawan.calendarproject.dto;

import java.util.Date;

import fr.dawan.calendarproject.enums.InterventionStatus;


public class InterventionDto implements Cloneable {

	private long id;
	
	private String comment;
	
	private LocationDto location;
	
	private CourseDto course;
	
	private AvancedUserDto user;
	
	private InterventionStatus type; 
	
	private boolean validated;

	private Date dateStart;

	private Date dateEnd;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public LocationDto getLocation() {
		return location;
	}

	public void setLocation(LocationDto location) {
		this.location = location;
	}
	
	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public InterventionStatus getType() {
		return type;
	}

	public void setType(InterventionStatus type) {
		this.type = type;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
