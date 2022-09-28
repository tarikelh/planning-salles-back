package fr.dawan.calendarproject.entities;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

@Entity
public class InterventionFollowed {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "student_id")
	User student;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "intervention_id")
	Intervention intervention;
	
	private String registrationSlug;
	
	@Version
	private int version;

	public InterventionFollowed(long id, User student, Intervention intervention, String registrationSlug,
			int version) {
		super();
		this.id = id;
		this.student = student;
		this.intervention = intervention;
		this.registrationSlug = registrationSlug;
		this.version = version;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public Intervention getIntervention() {
		return intervention;
	}

	public void setIntervention(Intervention intervention) {
		this.intervention = intervention;
	}
	

	public String getRegistrationSlug() {
		return registrationSlug;
	}

	public void setRegistrationSlug(String registrationSlug) {
		this.registrationSlug = registrationSlug;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "InterventionsFollowed [id=" + id + ", student=" + student + ", intervention=" + intervention
				+ ", registrationSlug=" + registrationSlug + ", version=" + version + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(intervention, student);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InterventionFollowed other = (InterventionFollowed) obj;
		return Objects.equals(intervention, other.intervention) && Objects.equals(student, other.student);
	}

	
	
	
	

}
