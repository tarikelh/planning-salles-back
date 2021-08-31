package fr.dawan.calendarproject.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class InterventionDto implements Cloneable {

	private long id;

	private String comment;

	private long locationId;

	private long courseId;

	private long userId;

	private String type;

	private boolean validated;

	private LocalDate dateStart;

	private LocalDate dateEnd;
	
	private LocalTime timeStart;
	
	private LocalTime timeEnd;

	private long masterInterventionId;

	private boolean isMaster;
	
	private int version;

	public InterventionDto() {
	}

	public InterventionDto(long id, String comment, long locationId, long courseId, long userId, String type,
			boolean validated, LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd, long masterInterventionId, boolean isMaster,
			int version) {
		this.id = id;
		this.comment = comment;
		this.locationId = locationId;
		this.courseId = courseId;
		this.userId = userId;
		this.type = type;
		this.validated = validated;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
		this.masterInterventionId = masterInterventionId;
		this.isMaster = isMaster;
		this.version = version;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getMasterInterventionId() {
		return masterInterventionId;
	}

	public void setMasterInterventionId(long masterId) {
		this.masterInterventionId = masterId;
	}

	public boolean isMaster() {
		return isMaster;
	}

	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
