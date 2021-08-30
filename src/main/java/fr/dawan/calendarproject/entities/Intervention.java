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
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import fr.dawan.calendarproject.enums.InterventionStatus;

@Entity
public class Intervention implements Diffable<Intervention> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = true, length = 255)
	private String comment;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Location location;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Course course;

	@ManyToOne(cascade = CascadeType.MERGE)
	private User user;

	@NotNull
	@Enumerated(EnumType.STRING)
	private InterventionStatus type;

	@Column(nullable = false)
	private boolean validated;

	// dateDebut, dateFin, many interv to one Employe, many interv. to one
	@Column(nullable = false, columnDefinition = "DATE")
	private LocalDate dateStart;

	// OU - @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(nullable = false, columnDefinition = "DATE")
	private LocalDate dateEnd;

	@ManyToOne
	private Intervention masterIntervention;

	private boolean isMaster;

	@Version
	private int version;

	// Constructor important pour la sérialization (exemple Jackson)
	public Intervention() throws Exception {
	}

	public Intervention(long id, String comment, Location location, Course course, User user, InterventionStatus type,
			boolean validated, LocalDate dateStart, LocalDate dateEnd, boolean isMaster,
			Intervention masterIntervention, int version) throws Exception {
		setId(id);
		setComment(comment);
		setLocation(location);
		setCourse(course);
		setUser(user);
		setType(type);
		setValidated(validated);
		setDateStart(dateStart);
		setDateEnd(dateEnd);
		setMasterIntervention(masterIntervention);
		setMaster(isMaster);
		setVersion(version);
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

	public void setDateEnd(LocalDate dateEnd) throws IllegalArgumentException {
		this.dateEnd = dateEnd;
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
	
	public void setEnumType(String type) {
		this.type = InterventionStatus.valueOf(type);
	}
	
	public String getEnumType() {
		return this.type.toString();
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

	@Override
	public DiffResult<Intervention> diff(Intervention obj) {
		return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
			       .append("comment", this.comment, obj.comment)
			       .append("location", this.location.getId(), obj.location.getId())
			       .append("course", this.course.getId(), obj.course.getId())
			       .append("user", this.user.getId(), obj.user.getId())
			       .append("type", this.type, obj.type)
			       .append("validated", this.validated, obj.validated)
			       .append("dateStart", this.dateStart, obj.dateStart)
			       .append("dateEnd", this.dateEnd, obj.dateEnd)
			       .append("masterIntervention", this.masterIntervention, obj.masterIntervention)
			       .append("isMaster", this.isMaster, obj.isMaster)
			       .build();
	}
}
