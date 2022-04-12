package fr.dawan.calendarproject.dto;

public class LeavePeriodDto {

	private long id;
	private long employeeId;
	private String slug;
	private String type;
	private String firstDay;
	private boolean startsAfternoon;
	private String lastDay;
	private boolean endsAfternoon;
	private double days;
	private String comments;

	public LeavePeriodDto() {
		super();
	}

	public LeavePeriodDto(long id, long employeeId, String slug, String type, String firstDay, boolean startsAfternoon,
                    String lastDay, boolean endsAfternoon, double days, String comments) {
		super();
		this.id = id;
		this.employeeId = employeeId;
		this.slug = slug;
		this.type = type;
		this.firstDay = firstDay;
		this.startsAfternoon = startsAfternoon;
		this.lastDay = lastDay;
		this.endsAfternoon = endsAfternoon;
		this.days = days;
		this.comments = comments;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
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

	public String getFirstDay() {
		return firstDay;
	}

	public void setFirstDay(String firstDay) {
		this.firstDay = firstDay;
	}

	public boolean isStartsAfternoon() {
		return startsAfternoon;
	}

	public void setStartsAfternoon(boolean startsAfternoon) {
		this.startsAfternoon = startsAfternoon;
	}

	public String getLastDay() {
		return lastDay;
	}

	public void setLastDay(String lastDay) {
		this.lastDay = lastDay;
	}

	public boolean isEndsAfternoon() {
		return endsAfternoon;
	}

	public void setEndsAfternoon(boolean endsAfternoon) {
		this.endsAfternoon = endsAfternoon;
	}

	public double getDays() {
		return days;
	}

	public void setDays(double days) {
		this.days = days;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
