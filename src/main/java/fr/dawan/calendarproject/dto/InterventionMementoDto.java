package fr.dawan.calendarproject.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class InterventionMementoDto implements Cloneable {

	private long interventionId;
	
	private String comment;
	
	private long locationId;

	@Transient
	private String locationCity;
	
	private long courseId;
	
	@Transient
	private String courseTitle;
	
	private long userId;

	@Transient
	private String userEmail;

	@Transient
	private String userFullName;
	
	private String type; 
	
	private boolean validated;

	private LocalDate dateStart;

	private LocalDate dateEnd;
	
	private LocalTime timeStart;
	
	private LocalTime timeEnd;
	
	private long masterInterventionId;
	
	private boolean isMaster;

	public InterventionMementoDto() {
	}

	public InterventionMementoDto(long interventionId, String comment, long locationId, String locationCity,
			long courseId, String courseTitle, long userId, String userEmail, String userFullName,
			String type, boolean validated, LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart,
			LocalTime timeEnd, long masterInterventionId, boolean isMaster) {
		this.interventionId = interventionId;
		this.comment = comment;
		this.locationId = locationId;
		this.locationCity = locationCity;
		this.courseId = courseId;
		this.courseTitle = courseTitle;
		this.userId = userId;
		this.userEmail = userEmail;
		this.userFullName = userFullName;
		this.type = type;
		this.validated = validated;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
		this.masterInterventionId = masterInterventionId;
		this.isMaster = isMaster;
	}

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


	public String getLocationCity() {
		return locationCity;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
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


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public boolean getValidated() {
		return validated;
	}


	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public long getMasterInterventionId() {
		return masterInterventionId;
	}


	public void setMasterInterventionId(long masterInterventionId) {
		this.masterInterventionId = masterInterventionId;
	}

	public boolean isMaster() {
		return isMaster;
	}


	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	public LocalTime getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(LocalTime timeStart) {
		this.timeStart = timeStart;
	}

	public LocalTime getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(LocalTime timeEnd) {
		this.timeEnd = timeEnd;
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
		builder.append(validated);
		builder.append(";");
		builder.append(dateStart);
		builder.append(";");
		builder.append(dateEnd);
		builder.append(";");
		builder.append(masterInterventionId);
		builder.append(";");
		builder.append(isMaster);
		return builder.toString();
	}
	
}
