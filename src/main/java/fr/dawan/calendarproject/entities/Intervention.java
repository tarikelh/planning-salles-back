package fr.dawan.calendarproject.entities;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import fr.dawan.calendarproject.enums.InterventionStatus;

@Entity
public class Intervention {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = true, length = 255)
	private String planningComment;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Location location;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Course course;

	// @JoinColumn(name = "user_id", nullable = false) //nom par defaut
	@ManyToOne(cascade = CascadeType.PERSIST)
	private User user;

	@Enumerated(EnumType.STRING)
	private InterventionStatus status;

	// dateDebut, dateFin, many interv to one Employe, many interv. to one
	@Column(nullable = true)
	private LocalDate dateStart;

	// OU - @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(nullable = true)
	private LocalDate dateEnd;

	@Version
	private int version;

	// Constructor important pour la sérialization (exemple Jackson)
	public Intervention() {
	}

	public Intervention(String planningComment, Location location, Course course, User user, InterventionStatus status,
			LocalDate dateStart, LocalDate dateEnd) {
		setPlanningComment(planningComment);
		setLocation(location);
		setCourse(course);
		setUser(user);
		setStatus(status);
		setDateStart(dateStart);
		setDateEnd(dateEnd);
	}

	public Intervention(String planningComment, Location location, Course course, User user, LocalDate dateStart,
			LocalDate dateEnd) {
		setPlanningComment(planningComment);
		setLocation(location);
		setCourse(course);
		setUser(user);
		setDateStart(dateStart);
		setDateEnd(dateEnd);
	}

	public long getId() {
		return id;
	}

	public String getPlanningComment() {
		return planningComment;
	}

	public void setPlanningComment(String planningComment) {
		this.planningComment = planningComment;
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

	public InterventionStatus getStatus() {
		return status;
	}

	public void setStatus(InterventionStatus status) {
		this.status = status;
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
		return "Intervention [id=" + getId() + ", planningComment=" + getPlanningComment() + ", location="
				+ getLocation() + ", course=" + getCourse() + ", user=" + getUser() + ", status=" + getStatus()
				+ ", dateStart=" + getDateStart() + ", dateEnd=" + getDateEnd() + ", version=" + getVersion() + "]";
	}

}
