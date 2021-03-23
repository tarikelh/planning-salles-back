package fr.dawan.calendarproject.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.dawan.calendarproject.enums.InterventionStatus;

//Cloneable for the Momento
@Entity
public class Intervention {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = true, length = 255)
	private String comment;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Location location;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Course course;

	// @JoinColumn(name = "user_id", nullable = false) //nom par defaut
	@ManyToOne(cascade = CascadeType.MERGE)
	private User user;

	@Enumerated(EnumType.STRING)
	private InterventionStatus type;
	
	@Column(nullable = false)
	private boolean validated;

	// dateDebut, dateFin, many interv to one Employe, many interv. to one
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dateStart;

	// OU - @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dateEnd;

	@Version
	private int version;

	// Constructor important pour la sérialization (exemple Jackson)
	public Intervention() {
	}

	

	public Intervention(long id, String comment, Location location, Course course, User user, InterventionStatus type,
			boolean validated, Date dateStart, Date dateEnd) {
		setId(id);
		setComment(comment);
		setLocation(location);
		setCourse(course);
		setUser(user);
		setType(type);
		setValidated(validated);
		setDateStart(dateStart);
		setDateEnd(dateEnd);
	}
	
	
	public Intervention(long id, String comment, Location location, Course course, User user, InterventionStatus type,
			boolean validated, Date dateStart, Date dateEnd, int version) {
		this.id = id;
		this.comment = comment;
		this.location = location;
		this.course = course;
		this.user = user;
		this.type = type;
		this.validated = validated;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public InterventionStatus getType() {
		return type;
	}



	public void setType(InterventionStatus type) {
		this.type = type;
	}



	public boolean isValidated() {
		return validated;
	}


	public void setValidated(boolean validated) {
		this.validated = validated;
	}


	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) throws IllegalArgumentException {
		this.dateEnd = dateEnd;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Intervention other = (Intervention) obj;
		if (id != other.id)
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Intervention [id=" + id + ", comment=" + comment + ", location=" + location + ", course=" + course
				+ ", user=" + user + ", type=" + type + ", validated=" + validated + ", dateStart=" + dateStart
				+ ", dateEnd=" + dateEnd + ", version=" + version + "]";
	}

	
}
