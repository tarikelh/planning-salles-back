package fr.dawan.calendarproject.entities;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.persistence.JoinColumn;

@Entity
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToMany
	@JoinTable(name="room_booking", joinColumns = @JoinColumn(name = "booking_id"), inverseJoinColumns = @JoinColumn(name = "room_id"))
	private List<Room> rooms;
	
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


	

	public Booking(long id, List<Room> rooms, Intervention intervention, LocalDate beginDate, LocalDate endingDate,
			int version) {
		super();
		this.id = id;
		this.rooms = rooms;
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


	public List<Room> getRooms() {
		return rooms;
	}




	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
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
				&& Objects.equals(rooms, other.rooms) && version == other.version;
	}




	@Override
	public String toString() {
		return "Booking [id=" + id + ", rooms=" + rooms + ", intervention=" + intervention + ", beginDate=" + beginDate
				+ ", endingDate=" + endingDate + ", version=" + version + "]";
	}




}
