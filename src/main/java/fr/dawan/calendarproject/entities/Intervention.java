package fr.dawan.calendarproject.entities;

import java.time.LocalDate;
import java.time.LocalTime;

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
import javax.validation.constraints.NotNull;

import fr.dawan.calendarproject.enums.InterventionStatus;

@Entity
public class Intervention implements Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = true)
	private long idDg2;

	@Column(nullable = true, length = 255, unique = true)
	private String slug;

	@Column(nullable = true, length = 255)
	private String comment;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Location location;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Course course;

	@ManyToOne(cascade = CascadeType.MERGE)
	private User user;

	private int attendeesCount;

	@NotNull
	@Enumerated(EnumType.STRING)
	private InterventionStatus type;

	@Column(nullable = false)
	private boolean validated;

	@Column(nullable = false, columnDefinition = "DATE")
	private LocalDate dateStart;

	@Column(nullable = false, columnDefinition = "DATE")
	private LocalDate dateEnd;

	@Column(nullable = true, columnDefinition = "TIME")
	private LocalTime timeStart;

	@Column(nullable = true, columnDefinition = "TIME")
	private LocalTime timeEnd;

	@ManyToOne
	private Intervention masterIntervention;

	private boolean isMaster;

	@Version
	private int version;

	public Intervention() {
	}

	public Intervention(long id, long idDg2, String slug, String comment, Location location, Course course, User user,
			int attendeesCount, @NotNull InterventionStatus type, boolean validated, LocalDate dateStart,
			LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd, Intervention masterIntervention,
			boolean isMaster, int version) {
		super();
		this.id = id;
		this.idDg2 = idDg2;
		this.slug = slug;
		this.comment = comment;
		this.location = location;
		this.course = course;
		this.user = user;
		this.attendeesCount = attendeesCount;
		this.type = type;
		this.validated = validated;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
		this.masterIntervention = masterIntervention;
		this.isMaster = isMaster;
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

	public LocalDate getDateStart() {
		return dateStart;
	}

	public void setDateStart(LocalDate dateStart) {
		this.dateStart = dateStart;
	}

	public LocalDate getDateEnd() {
		return dateEnd;
	}

	public long getIdDg2() {
		return idDg2;
	}

	public void setIdDg2(long idDg2) {
		this.idDg2 = idDg2;
	}

	public void setDateEnd(LocalDate dateEnd) {
		this.dateEnd = dateEnd;
	}

	public LocalTime getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(LocalTime timeStart) {
		this.timeStart = timeStart;
	}

	public LocalTime getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(LocalTime timeEnd) {
		this.timeEnd = timeEnd;
	}

	public Intervention getMasterIntervention() {
		return masterIntervention;
	}

	public void setMasterIntervention(Intervention masterIntervention) {
		this.masterIntervention = masterIntervention;
	}

	public boolean isMaster() {
		return isMaster;
	}

	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public int getAttendeesCount() {
		return attendeesCount;
	}

	public void setAttendeesCount(int attendeesCount) {
		this.attendeesCount = attendeesCount;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + attendeesCount;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + ((dateEnd == null) ? 0 : dateEnd.hashCode());
		result = prime * result + ((dateStart == null) ? 0 : dateStart.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (idDg2 ^ (idDg2 >>> 32));
		result = prime * result + (isMaster ? 1231 : 1237);
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((masterIntervention == null) ? 0 : masterIntervention.hashCode());
		result = prime * result + ((slug == null) ? 0 : slug.hashCode());
		result = prime * result + ((timeEnd == null) ? 0 : timeEnd.hashCode());
		result = prime * result + ((timeStart == null) ? 0 : timeStart.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + (validated ? 1231 : 1237);
		result = prime * result + version;
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
		if (idDg2 != other.idDg2)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Intervention [id=");
		builder.append(id);
		builder.append(", idDg2=");
		builder.append(idDg2);
		builder.append(", slug=");
		builder.append(slug);
		builder.append(", comment=");
		builder.append(comment);
		builder.append(", location=");
		builder.append(location);;
		builder.append(", course=");
		builder.append(course);
		builder.append(", user=");
		builder.append(user);
		builder.append(", attendeesCount=");
		builder.append(attendeesCount);
		builder.append(", type=");
		builder.append(type);
		builder.append(", validated=");
		builder.append(validated);
		builder.append(", dateStart=");
		builder.append(dateStart);
		builder.append(", dateEnd=");
		builder.append(dateEnd);
		builder.append(", timeStart=");
		builder.append(timeStart);
		builder.append(", timeEnd=");
		builder.append(timeEnd);
		builder.append(", masterIntervention=");
		builder.append(masterIntervention);
		builder.append(", isMaster=");
		builder.append(isMaster);
		builder.append(", version=");
		builder.append(version);
		builder.append("]");
		return builder.toString();
	}


	public String toContentString() {
		return "Intervention " + course.getTitle() + " du " + dateStart.toString() + " au " + dateEnd.toString()
				+ " avec " + user.getFullname();
	}

	@Override
	public Intervention clone() {
		Intervention intervention = null;
		try {
			intervention = (Intervention) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		if (intervention != null) {
			return intervention;
		}
		return intervention;
	}
}
