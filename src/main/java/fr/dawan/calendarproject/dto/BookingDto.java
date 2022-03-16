package fr.dawan.calendarproject.dto;

import java.time.LocalDate;

public class BookingDto {
	
	private long id ;
	
	private long referance;
	
	private LocalDate dateStart;
	
	private LocalDate dateEnd;
	
	private long roomId;
	
	private long interventionId;
	
	private int version;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getReferance() {
		return referance;
	}

	public void setReferance(long referance) {
		this.referance = referance;
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

	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
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

	public BookingDto(long id, long referance, LocalDate dateStart, LocalDate dateEnd, long roomId, long interventionId,
			int version) {
		super();
		this.id = id;
		this.referance = referance;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.roomId = roomId;
		this.interventionId = interventionId;
		this.version = version;
	}

	public BookingDto() {
		super();
	}
	
	

}