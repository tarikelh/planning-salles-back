package fr.dawan.calendarproject.dto;

public class LeavePeriodDto {

	private long id;
	private long idDg2;
	private long employeeId;
	private String employeeFullName;
	private String slug;
	private String type;
	private String firstDay;
	private boolean startsAfternoon;
	private String lastDay;
	private boolean endsAfternoon;
	private double days;
	private String comments;
	private int version;

	public LeavePeriodDto() {
		super();
	}

	public LeavePeriodDto(long id, long idDg2, long employeeId, String employeeFullName, String slug, String type, String firstDay, boolean startsAfternoon,
                    String lastDay, boolean endsAfternoon, double days, String comments, int version) {
		this.id = id;
		this.idDg2 = idDg2;
		this.employeeId = employeeId;
		this.employeeFullName = employeeFullName;
		this.slug = slug;
		this.type = type;
		this.firstDay = firstDay;
		this.startsAfternoon = startsAfternoon;
		this.lastDay = lastDay;
		this.endsAfternoon = endsAfternoon;
		this.days = days;
		this.comments = comments;
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

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeFullName() {
		return employeeFullName;
	}

	public void setEmployeeFullName(String employeeFullName) {
		this.employeeFullName = employeeFullName;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
