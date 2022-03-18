package fr.dawan.calendarproject.dto;

import java.time.LocalDate;

public class BookingDto {
	
	private long id ;
	
	private LocalDate dateStart;
	
	private LocalDate dateEnd;
	
	private long interventionId;
	
	private int version;

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

	



	public long getInterventionId() {
		return interventionId;
	}

	public void setInterventionId(long interventionId) {
		this.interventionId = interventionId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public BookingDto(long id, LocalDate dateStart, LocalDate dateEnd, long interventionId,
			int version) {
		super();
		this.id = id;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		
		this.interventionId = interventionId;
		this.version = version;
	}

	public BookingDto() {
		super();
	}
	
	

}