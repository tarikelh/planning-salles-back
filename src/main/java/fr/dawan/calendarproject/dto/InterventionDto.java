package fr.dawan.calendarproject.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class InterventionDto implements Cloneable {

	private long id;
	
	private long idDg2;
	
	private String slug;

	private String comment;

	private long locationId;
	
	private long locationIdDg2;

	private long courseId;
	
	private long courseIdDg2;

	private long userId;
	
	private int attendeesCount;

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

	public InterventionDto(long id, long idDg2, String slug, String comment, long locationId, long locationIdDg2,
			long courseId, long courseIdDg2, long userId, int attendeesCount, String type, boolean validated,
			LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd, long masterInterventionId,
			boolean isMaster, int version) {
		super();
		this.id = id;
		this.idDg2 = idDg2;
		this.slug = slug;
		this.comment = comment;
		this.locationId = locationId;
		this.locationIdDg2 = locationIdDg2;
		this.courseId = courseId;
		this.courseIdDg2 = courseIdDg2;
		this.userId = userId;
		this.attendeesCount = attendeesCount;
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

	public long getIdDg2() {
		return idDg2;
	}

	public void setIdDg2(long idDg2) {
		this.idDg2 = idDg2;
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

	public long getLocationIdDg2() {
		return locationIdDg2;
	}

	public void setLocationIdDg2(long locationIdDg2) {
		this.locationIdDg2 = locationIdDg2;
	}

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	public long getCourseIdDg2() {
		return courseIdDg2;
	}

	public void setCourseIdDg2(long courseIdDg2) {
		this.courseIdDg2 = courseIdDg2;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + attendeesCount;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + (int) (courseId ^ (courseId >>> 32));
		result = prime * result + ((dateEnd == null) ? 0 : dateEnd.hashCode());
		result = prime * result + ((dateStart == null) ? 0 : dateStart.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (idDg2 ^ (idDg2 >>> 32));
		result = prime * result + (isMaster ? 1231 : 1237);
		result = prime * result + (int) (locationId ^ (locationId >>> 32));
		result = prime * result + (int) (masterInterventionId ^ (masterInterventionId >>> 32));
		result = prime * result + ((slug == null) ? 0 : slug.hashCode());
		result = prime * result + ((timeEnd == null) ? 0 : timeEnd.hashCode());
		result = prime * result + ((timeStart == null) ? 0 : timeStart.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + (int) (userId ^ (userId >>> 32));
		result = prime * result + (validated ? 1231 : 1237);
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InterventionDto other = (InterventionDto) obj;
		if (attendeesCount != other.attendeesCount)
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (courseId != other.courseId)
			return false;
		if (dateEnd == null) {
			if (other.dateEnd != null)
				return false;
		} else if (!dateEnd.equals(other.dateEnd))
			return false;
		if (dateStart == null) {
			if (other.dateStart != null)
				return false;
		} else if (!dateStart.equals(other.dateStart))
			return false;
		if (id != other.id)
			return false;
		if (idDg2 != other.idDg2)
			return false;
		if (isMaster != other.isMaster)
			return false;
		if (locationId != other.locationId)
			return false;
		if (masterInterventionId != other.masterInterventionId)
			return false;
		if (slug == null) {
			if (other.slug != null)
				return false;
		} else if (!slug.equals(other.slug))
			return false;
		if (timeEnd == null) {
			if (other.timeEnd != null)
				return false;
		} else if (!timeEnd.equals(other.timeEnd))
			return false;
		if (timeStart == null) {
			if (other.timeStart != null)
				return false;
		} else if (!timeStart.equals(other.timeStart))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (userId != other.userId)
			return false;
		if (validated != other.validated)
			return false;
		if (version != other.version)
			return false;
		return true;
	}
	
}

