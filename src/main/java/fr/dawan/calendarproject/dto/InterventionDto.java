package fr.dawan.calendarproject.dto;

import java.time.LocalDate;

import fr.dawan.calendarproject.enums.InterventionStatus;

public class InterventionDto implements Cloneable {

	private long id;

	private String comment;

	private long locationId;

	private long courseId;

	private long userId;

	private InterventionStatus type;

	private boolean validated;

	private LocalDate dateStart;

	private LocalDate dateEnd;

	private long nextInterventionId;

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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public long getNextInterventionId() {
		return nextInterventionId;
	}

	public void setNextInterventionId(long nextInterventionId) {
		this.nextInterventionId = nextInterventionId;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
