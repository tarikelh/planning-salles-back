package fr.dawan.calendarproject.dto;

import java.time.LocalDate;

public class DateRangeDto {

	private LocalDate start;
	private LocalDate end;
	
	public DateRangeDto() {
	}
	
	public DateRangeDto(LocalDate start, LocalDate end) {
		this.start = start;
		this.end = end;
	}
	
	public LocalDate getStart() {
		return start;
	}
	
	public void setStart(LocalDate start) {
		this.start = start;
	}
	
	public LocalDate getEnd() {
		return end;
	}
	
	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public boolean isOverlapping(DateRangeDto toCheck ) {
		LocalDate toCheckStart = toCheck.getStart();
		LocalDate toCheckEnd = toCheck.getEnd();
		
		if ((start.isAfter(toCheckStart) || start.isEqual(toCheckStart))
				&& (start.isBefore(toCheckEnd) || start.isEqual(toCheckEnd))) {
			return true;
		}
		else if ((end.isAfter(toCheckStart) || end.isEqual(toCheckStart))
				&& (end.isBefore(toCheckEnd) || end.isEqual(toCheckEnd))) {
			return true;
		}
		
		return false;
	}
}
