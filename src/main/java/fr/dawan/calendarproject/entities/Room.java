package fr.dawan.calendarproject.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
public class Room extends Resource{

	private long idDg2;
	
	private int fullCapacity;
	
	private int reducedCapacity;
	
	@ManyToMany(mappedBy = "rooms")
	Set<Booking> books;
	
	@ManyToMany(mappedBy = "rooms")
	Set<Equipment> equipments;
	
	
	
	public long getIdDg2() {
		return idDg2;
	}



	public void setIdDg2(long idDg2) {
		this.idDg2 = idDg2;
	}



	public int getFullCapacity() {
		return fullCapacity;
	}



	public void setFullCapacity(int fullCapacity) {
		this.fullCapacity = fullCapacity;
	}



	public int getReducedCapacity() {
		return reducedCapacity;
	}



	public void setReducedCapacity(int reducedCapacity) {
		this.reducedCapacity = reducedCapacity;
	}



	public Room(long id, String reference, String name, boolean available, int version, long idDg2, int fullCapacity, int reducedCapacity) {
		super(id, reference, name, available, version);
		setIdDg2(idDg2);
		setFullCapacity(fullCapacity);
		setReducedCapacity(reducedCapacity);
	}

}
