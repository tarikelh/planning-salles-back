package fr.dawan.calendarproject.entities;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;


@Entity
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = true)
	private long taskIdDg2;
	
	private String title;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	private User user;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Intervention intervention;
	
	@Column(nullable = false)
	private String slug;
	
	@Column(nullable= false, columnDefinition="DATE")
	private LocalDate beginDate;
	
	@Column(nullable= false, columnDefinition="DATE")
	private LocalDate endDate;
	
	@Column(nullable=false)
	private double duration;
	
	@Version
	private int version;
	
	

	public Task() {

	}

	public Task(long id, long taskIdDg2, String title, User user, Intervention intervention, String slug,
			LocalDate beginDate, LocalDate endDate, double duration, int version) {
		super();
		this.id = id;
		this.taskIdDg2 = taskIdDg2;
		this.title = title;
		this.user = user;
		this.intervention = intervention;
		this.slug = slug;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.duration = duration;
		this.version = version;
	}


	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}


	public long getTaskIdDg2() {
		return taskIdDg2;
	}

	public void setTaskIdDg2(long taskIdDg2) {
		this.taskIdDg2 = taskIdDg2;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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



	public String getSlug() {
		return slug;
	}



	public void setSlug(String slug) {
		this.slug = slug;
	}



	public LocalDate getBeginDate() {
		return beginDate;
	}



	public void setBeginDate(LocalDate beginDate) {
		this.beginDate = beginDate;
	}



	public LocalDate getEndDate() {
		return endDate;
	}



	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}


	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}



	public int getVersion() {
		return version;
	}



	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		return Objects.hash(beginDate, duration, endDate, id, intervention, slug, taskIdDg2, title, user, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		return Objects.equals(beginDate, other.beginDate)
				&& Double.doubleToLongBits(duration) == Double.doubleToLongBits(other.duration)
				&& Objects.equals(endDate, other.endDate) && id == other.id
				&& Objects.equals(intervention, other.intervention) && Objects.equals(slug, other.slug)
				&& taskIdDg2 == other.taskIdDg2 && Objects.equals(title, other.title)
				&& Objects.equals(user, other.user) && version == other.version;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", taskIdDg2=" + taskIdDg2 + ", title=" + title + ", user=" + user + ", intervention="
				+ intervention + ", slug=" + slug + ", beginDate=" + beginDate + ", endDate=" + endDate + ", duration="
				+ duration + ", version=" + version + "]";
	}

	
	
		
}
