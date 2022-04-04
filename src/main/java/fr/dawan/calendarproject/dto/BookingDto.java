package fr.dawan.calendarproject.dto;

import java.time.LocalDate;
import java.util.Objects;


public class BookingDto implements Cloneable{
	
	private long id ;
	
	private LocalDate dateStart;
	
	private LocalDate dateEnd;
	
	private long roomId;
	
	private long BookingDtoId;
	
	private int version;
	
	public BookingDto() {
		super();
	}
	

	public BookingDto(long id, LocalDate dateStart, LocalDate dateEnd, long roomId, long BookingDtoId, int version) {
		super();
		this.id = id;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.roomId = roomId;
		this.BookingDtoId = BookingDtoId;
		this.version = version;
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

	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public long getInterventionId() {
		return BookingDtoId;
	}

	public void setInterventionId(long BookingDtoId) {
		this.BookingDtoId = BookingDtoId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}


	


	@Override
	public int hashCode() {
		return Objects.hash(dateEnd, dateStart, BookingDtoId, roomId, version);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookingDto other = (BookingDto) obj;
		return Objects.equals(dateEnd, other.dateEnd) && Objects.equals(dateStart, other.dateStart)
				&& BookingDtoId == other.BookingDtoId && roomId == other.roomId && version == other.version;
	}


	@Override
	public String toString() {
		return "BookingDto [id=" + id + ", dateStart=" + dateStart + ", dateEnd=" + dateEnd + ", roomId=" + roomId
				+ ", BookingDtoId=" + BookingDtoId + ", version=" + version + "]";
	}


	@Override
	public BookingDto clone() {
		
		BookingDto BookingDto = null;
		try {
			BookingDto = (BookingDto) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		if (BookingDto != null) {
			return BookingDto;
		}
		return BookingDto;

	}

	
	
	
	

}