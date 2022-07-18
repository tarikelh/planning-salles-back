package fr.dawan.calendarproject.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AdvancedInterventionDto {

	private long id;

	private long idDg2;

	private String slug;

	private String comment;

	private LocationDto location;

	private CourseDto course;

	private UserDto user;

	private int attendeesCount;

	private String type;

	private boolean validated;

	private LocalDate dateStart;

	private LocalDate dateEnd;

	private LocalTime timeStart;

	private LocalTime timeEnd;

	private InterventionDto masterIntervention;

	private boolean isMaster;

	private String customers;

	private int version;

	public AdvancedInterventionDto() {
	}

	public AdvancedInterventionDto(long id, long idDg2, String slug, String comment, LocationDto location,
			CourseDto course, UserDto user, int attendeesCount, String type, boolean validated, LocalDate dateStart,
			LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd, InterventionDto masterIntervention,
			boolean isMaster, String customers, int version) {
		super();
		this.id = id;
		this.idDg2 = idDg2;
		this.slug = slug;
		this.comment = comment;
		this.location = location;
		this.course = course;
		this.user = user;
		this.attendeesCount = attendeesCount;
		this.type = type;
		this.validated = validated;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
		this.masterIntervention = masterIntervention;
		this.isMaster = isMaster;
		this.customers = customers;
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

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocationDto getLocation() {
		return location;
	}

	public void setLocation(LocationDto location) {
		this.location = location;
	}

	public CourseDto getCourse() {
		return course;
	}

	public void setCourse(CourseDto course) {
		this.course = course;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public int getAttendeesCount() {
		return attendeesCount;
	}

	public void setAttendeesCount(int attendeesCount) {
		this.attendeesCount = attendeesCount;
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

	public InterventionDto getMasterIntervention() {
		return masterIntervention;
	}

	public void setMasterIntervention(InterventionDto masterIntervention) {
		this.masterIntervention = masterIntervention;
	}

	public boolean isMaster() {
		return isMaster;
	}

	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	public String getCustomers() {
		return customers;
	}

	public void setCustomers(String customers) {
		this.customers = customers;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}