package fr.dawan.calendarproject.entities;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Version;

@Entity
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column
	private LocalDate dateStart;
	
	@Column
	private LocalDate dateEnd;
	
	@Version
	private int version;
	
	@ManyToMany
	private Set<Room> rooms;

	
	public Booking() {
		super();
	}


	public Booking(long id, LocalDate dateStart, LocalDate dateEnd, int version) {
		super();
		this.id = id;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
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


	public int getVersion() {
		return version;
	}


	public void setVersion(int version) {
		this.version = version;
	}
	
	
	
	
}
