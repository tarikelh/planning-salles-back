package fr.dawan.calendarproject.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import fr.dawan.calendarproject.enums.InterventionStatus;

@Entity
@BatchSize(size = 100)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
public class Intervention implements Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = true)
	private long idDg2;

	@Column(nullable = false, length = 255, unique = true)
	private String slug;

	@Column(nullable = true, length = 255, unique = true)
	private String optionSlug;
	
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
	
	private long masterInterventionIdTemp;
	
	@Column(columnDefinition = "TEXT")
	private String customers;
	
	//ToDo check if we need getter and setter on dto
	@OneToMany(mappedBy = "intervention")
	Set<InterventionsFollowed> interventionsFollowed;

	@Version
	private int version;

	public Intervention() {
	}
	
	public Intervention(long id, long idDg2, String slug, String optionSlug, String comment, Location location,
			Course course, User user, int attendeesCount, @NotNull InterventionStatus type, boolean validated,
			LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd,
			Intervention masterIntervention, boolean isMaster, long masterInterventionIdTemp, String customers,
			Set<InterventionsFollowed> interventionsFollowed, int version) {
		super();
		this.id = id;
		this.idDg2 = idDg2;
		this.slug = slug;
		this.optionSlug = optionSlug;
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
		this.masterInterventionIdTemp = masterInterventionIdTemp;
		this.customers = customers;
		this.interventionsFollowed = interventionsFollowed;
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

	public long getMasterInterventionIdTemp() {
		return masterInterventionIdTemp;
	}

	public void setMasterInterventionIdTemp(long masterInterventionIdTemp) {
		this.masterInterventionIdTemp = masterInterventionIdTemp;
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

	public String getOptionSlug() {
		return optionSlug;
	}

	public void setOptionSlug(String optionSlug) {
		this.optionSlug = optionSlug;
	}

	public int getAttendeesCount() {
		return attendeesCount;
	}

	public void setAttendeesCount(int attendeesCount) {
		this.attendeesCount = attendeesCount;
	}
	
	public String getCustomers() {
		return customers;
	}

	public void setCustomers(String customers) {
		this.customers = customers;
	}

	public long getCourseId() {
		return getCourse().getId();
	}
	
	public long getUserId() {
		return getUser().getId();
	}
	
	public Set<InterventionsFollowed> getInterventionsFollowed() {
		return interventionsFollowed;
	}

	public void setInterventionsFollowed(Set<InterventionsFollowed> interventionsFollowed) {
		this.interventionsFollowed = interventionsFollowed;
	}

	@Override
	public int hashCode() {
		return Objects.hash(comment, course, customers, dateEnd, dateStart, interventionsFollowed, location, slug, type,
				user, validated);
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
		return Objects.equals(comment, other.comment) && Objects.equals(course, other.course)
				&& Objects.equals(customers, other.customers) && Objects.equals(dateEnd, other.dateEnd)
				&& Objects.equals(dateStart, other.dateStart)
				&& Objects.equals(interventionsFollowed, other.interventionsFollowed)
				&& Objects.equals(location, other.location) && Objects.equals(slug, other.slug) && type == other.type
				&& Objects.equals(user, other.user) && validated == other.validated;
	}


	@Override
	public String toString() {
		return "Intervention [id=" + id + ", idDg2=" + idDg2 + ", slug=" + slug + ", optionSlug=" + optionSlug
				+ ", comment=" + comment + ", location=" + location + ", course=" + course + ", user=" + user
				+ ", attendeesCount=" + attendeesCount + ", type=" + type + ", validated=" + validated + ", dateStart="
				+ dateStart + ", dateEnd=" + dateEnd + ", timeStart=" + timeStart + ", timeEnd=" + timeEnd
				+ ", masterIntervention=" + masterIntervention + ", isMaster=" + isMaster
				+ ", masterInterventionIdTemp=" + masterInterventionIdTemp + ", customers=" + customers
				+ ", interventionsFollowed=" + interventionsFollowed + ", version=" + version + "]";
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

	public String getUserLastName() {
		return getUser().getLastName();
	}

	public String getUserFirstName() {
		return getUser().getFirstName();
	}
	
	public String getUserFullName() {
		return getUser().getFullname();
	}
}
