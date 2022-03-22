package fr.dawan.calendarproject.entities;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

@Entity

public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	private Room room;
	
	@ManyToOne(cascade= CascadeType.MERGE)
	private Intervention intervention;
	
	@Column(nullable= false, columnDefinition="DATE")
	private LocalDate beginDate;
	
	@Column(nullable= false, columnDefinition="DATE")
	private LocalDate endingDate;
	
	@Version
	private int version;

	public Booking() {
		super();
	}

	public Booking(long id, Room room, Intervention intervention, LocalDate beginDate, LocalDate endingDate,
			int version) {
		super();
		this.id = id;
		this.room = room;
		this.intervention = intervention;
		this.beginDate = beginDate;
		this.endingDate = endingDate;
		this.version = version;
	}


	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}


	public Room getRoom() {
		return room;
	}




	public void setRoom(Room room) {
		this.room = room;
	}




	public Intervention getIntervention() {
		return intervention;
	}



	public void setIntervention(Intervention intervention) {
		this.intervention = intervention;
	}



	public LocalDate getBeginDate() {
		return beginDate;
	}



	public void setBeginDate(LocalDate beginDate) {
		this.beginDate = beginDate;
	}



	public LocalDate getEndingDate() {
		return endingDate;
	}



	public void setEndingDate(LocalDate endingDate) {
		this.endingDate = endingDate;
	}



	public int getVersion() {
		return version;
	}



	public void setVersion(int version) {
		this.version = version;
	}




	@Override
	public int hashCode() {
		return Objects.hash(beginDate, endingDate, id, intervention, room, version);
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Booking other = (Booking) obj;
		return Objects.equals(beginDate, other.beginDate) && Objects.equals(endingDate, other.endingDate)
				&& id == other.id && Objects.equals(intervention, other.intervention)
				&& Objects.equals(room, other.room);
	}




	@Override
	public String toString() {
		return "Booking [id=" + id + ", room=" + room + ", intervention=" + intervention + ", beginDate=" + beginDate
				+ ", endingDate=" + endingDate + ", version=" + version + "]";
	}



}
