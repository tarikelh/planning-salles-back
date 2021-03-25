package fr.dawan.calendarproject.dto;

import java.util.Date;

import javax.persistence.Embeddable;

import fr.dawan.calendarproject.enums.InterventionStatus;

@Embeddable
public class InterventionMementoDto implements Cloneable {

	private long interventionId;
	
	private String comment;
	
	private long locationId;
	
	private long courseId;
	
	private long userId;
	
	private InterventionStatus type; 
	
	private boolean isValidated; //change to confirmStatus name

	private Date dateStart;

	private Date dateEnd;

	
	public long getInterventionId() {
		return interventionId;
	}


	public void setInterventionId(long interventionId) {
		this.interventionId = interventionId;
	}


	public long getLocationId() {
		return locationId;
	}


	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}


	public long getCourseId() {
		return courseId;
	}


	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}


	public long getUserId() {
		return userId;
	}


	public void setUserId(long userId) {
		this.userId = userId;
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


	public InterventionStatus getType() {
		return type;
	}


	public void setType(InterventionStatus type) {
		this.type = type;
	}


	public boolean isValidated() {
		return isValidated;
	}


	public void setValidated(boolean isValidated) {
		this.isValidated = isValidated;
	}


	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(interventionId);
		builder.append(";");
		builder.append(comment);
		builder.append(";");
		builder.append(locationId);
		builder.append(";");
		builder.append(courseId);
		builder.append(";");
		builder.append(userId);
		builder.append(";");
		builder.append(type);
		builder.append(";");
		builder.append(isValidated);
		builder.append(";");
		builder.append(dateStart);
		builder.append(";");
		builder.append(dateEnd);
		return builder.toString();
	}
	
}
