package fr.dawan.calendarproject.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
public class Equipment extends Resource{

	@ManyToMany
	private Set<Room>rooms;
	
	public Equipment(long id, String reference, String name, boolean available, int version) {
		super(id, reference, name, available, version);
		
	}

}
