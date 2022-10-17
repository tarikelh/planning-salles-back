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
	@JoinColumn(name = "user_id")
	User user;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "intervention_id")
	Intervention intervention;
	
	private String registrationSlug;
	
	@Version
	private int version;

	
	public InterventionFollowed() {
	}


	public InterventionFollowed(long id, User user, Intervention intervention, String registrationSlug, int version) {
		super();
		this.id = id;
		this.user = user;
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

	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
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
		return "InterventionFollowed [id=" + id + ", user=" + user + ", intervention=" + intervention
				+ ", registrationSlug=" + registrationSlug + ", version=" + version + "]";
	}


	@Override
	public int hashCode() {
		return Objects.hash(intervention, registrationSlug, user, version);
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
		return Objects.equals(intervention, other.intervention)
				&& Objects.equals(registrationSlug, other.registrationSlug) && Objects.equals(user, other.user)
				&& version == other.version;
	}



	
}
