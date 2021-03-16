package fr.dawan.calendarproject.dto;

import java.time.LocalDate;

public class InterventionDto {

	private long id;

	private LocalDate dateStart;

	private LocalDate dateEnd;

	private AvancedUserDto employee;

	private CourseDto course;

	private int version;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public InterventionDto() {
		course = new CourseDto();
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

	public AvancedUserDto getEmployee() {
		return employee;
	}

	public void setEmployee(AvancedUserDto employee) {
		this.employee = employee;
	}

	public CourseDto getCourse() {
		return course;
	}

	public void setCourse(CourseDto course) {
		this.course = course;
	}
}
