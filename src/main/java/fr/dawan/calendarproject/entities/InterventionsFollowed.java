package fr.dawan.calendarproject.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;



@Entity
public class InterventionsFollowed {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "student_id")
	User student;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "intervention_id")
	Intervention intervention;
	

}
