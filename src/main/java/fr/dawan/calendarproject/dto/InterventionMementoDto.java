package fr.dawan.calendarproject.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Embeddable;

@Embeddable
public class InterventionMementoDto implements Cloneable {

	private long interventionId;
	
	private long idDg2;
	
	private String slug;

	private String comment;

	private long locationId;

	private String locationCity;

	private long courseId;

	private String courseTitle;

	private long userId;

	private String userFullName;
	
	private int attendeesCount;

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

	public InterventionMementoDto(long interventionId, long idDg2, String slug, String comment, long locationId,
			String locationCity, long courseId, String courseTitle, long userId, String userFullName,
			int attendeesCount, String type, boolean validated, LocalDate dateStart, LocalDate dateEnd,
			LocalTime timeStart, LocalTime timeEnd, long masterInterventionId, boolean isMaster) {
		super();
		this.interventionId = interventionId;
		this.idDg2 = idDg2;
		this.slug = slug;
		this.comment = comment;
		this.locationId = locationId;
		this.locationCity = locationCity;
		this.courseId = courseId;
		this.courseTitle = courseTitle;
		this.userId = userId;
		this.userFullName = userFullName;
		this.attendeesCount = attendeesCount;
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

	public long getIdDg2() {
		return idDg2;
	}

	public void setIdDg2(long idDg2) {
		this.idDg2 = idDg2;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public int getAttendeesCount() {
		return attendeesCount;
	}

	public void setAttendeesCount(int attendeesCount) {
		this.attendeesCount = attendeesCount;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("interventionId=");
		builder.append(interventionId);
		builder.append(", idDg2=");
		builder.append(idDg2);
		builder.append(", slug=");
		builder.append(slug);
		builder.append(", comment=");
		builder.append(comment);
		builder.append(", locationId=");
		builder.append(locationId);
		builder.append(", locationCity=");
		builder.append(locationCity);
		builder.append(", courseId=");
		builder.append(courseId);
		builder.append(", courseTitle=");
		builder.append(courseTitle);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", userFullName=");
		builder.append(userFullName);
		builder.append(", attendeesCount=");
		builder.append(attendeesCount);
		builder.append(", type=");
		builder.append(type);
		builder.append(", validated=");
		builder.append(validated);
		builder.append(", dateStart=");
		builder.append(dateStart);
		builder.append(", dateEnd=");
		builder.append(dateEnd);
		builder.append(", timeStart=");
		builder.append(timeStart);
		builder.append(", timeEnd=");
		builder.append(timeEnd);
		builder.append(", masterInterventionId=");
		builder.append(masterInterventionId);
		builder.append(", isMaster=");
		builder.append(isMaster);
		return builder.toString();
	}

}
