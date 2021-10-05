package fr.dawan.calendarproject.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateRangeDto {

	private long interventionId;
	private LocalDate dateStart;
	private LocalDate dateEnd;
	private LocalTime timeStart;
	private LocalTime timeEnd;

	public DateRangeDto() {
	}

	public DateRangeDto(long interventionId, LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart,
			LocalTime timeEnd) {
		this.interventionId = interventionId;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
	}

	public long getInterventionId() {
		return interventionId;
	}

	public void setInterventionId(long interventionId) {
		this.interventionId = interventionId;
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

	public boolean isOverlapping(DateRangeDto toCheck) {
		LocalDate toCheckStart = toCheck.getDateStart();
		LocalDate toCheckEnd = toCheck.getDateEnd();
		
		if ((dateStart.isAfter(toCheckStart) || dateStart.isEqual(toCheckStart))
				&& (dateStart.isBefore(toCheckEnd) || dateStart.isEqual(toCheckEnd))) {
			return true;
		}
		else if ((dateEnd.isAfter(toCheckStart) || dateEnd.isEqual(toCheckStart))
				&& (dateEnd.isBefore(toCheckEnd) || dateEnd.isEqual(toCheckEnd))) {
			return true;
		}
		
		return false;
	}
}
