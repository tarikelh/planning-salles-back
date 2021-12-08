package fr.dawan.calendarproject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InterventionDG2Dto {
	private long id;
	private long locationId;
	private long courseId;
	private long userId;
	private String dateStart;
	private String dateEnd;
	private String slug;
	private String type;
	private boolean validated;
	private long masterInterventionId;
	private boolean isMaster;
	@JsonProperty("nbParticipants")
	private int attendeesCount;

	public InterventionDG2Dto() {
	}

	public InterventionDG2Dto(long id, long locationId, long courseId, long userId, String dateStart, String dateEnd,
			String slug, String type, boolean validated, long masterInterventionId, boolean isMaster,
			int attendeesCount) {
		this.id = id;
		this.locationId = locationId;
		this.courseId = courseId;
		this.userId = userId;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.slug = slug;
		this.type = type;
		this.validated = validated;
		this.masterInterventionId = masterInterventionId;
		this.isMaster = isMaster;
		this.attendeesCount = attendeesCount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isValidated() {
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

	public int getAttendeesCount() {
		return attendeesCount;
	}

	public void setAttendeesCount(int attendeesCount) {
		this.attendeesCount = attendeesCount;
	}

	public boolean isMaster() {
		return isMaster;
	}

	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}
}