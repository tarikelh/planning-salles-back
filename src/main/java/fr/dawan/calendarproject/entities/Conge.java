package fr.dawan.calendarproject.entities;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import fr.dawan.calendarproject.enums.CongeType;

@Entity
public class Conge {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long idDg2;
	private long employeeId;

	private String slug;
	private CongeType type;
	private LocalDateTime firstDay;
	private boolean startsAfternoon;
	private LocalDateTime lastDay;
	private boolean endsAfternoon;
	private double days;
	private String comments;

	public Conge() {
		super();
	}

	public Conge(long id, long idDg2, long employeeId, String slug, CongeType type, LocalDateTime firstDay,
			boolean startsAfternoon, LocalDateTime lastDay, boolean endsAfternoon, double days, String comments) {
		super();
		this.id = id;
		this.idDg2 = idDg2;
		this.employeeId = employeeId;
		this.slug = slug;
		this.type = type;
		this.firstDay = firstDay;
		this.startsAfternoon = startsAfternoon;
		this.lastDay = lastDay;
		this.endsAfternoon = endsAfternoon;
		this.days = days;
		this.comments = comments;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdDg2() {
		return idDg2;
	}

	public void setIdDg2(long idDg2) {
		this.idDg2 = idDg2;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public CongeType getType() {
		return type;
	}

	public void setType(CongeType type) {
		this.type = type;
	}

	public LocalDateTime getFirstDay() {
		return firstDay;
	}

	public void setFirstDay(LocalDateTime firstDay) {
		this.firstDay = firstDay;
	}

	public boolean isStartsAfternoon() {
		return startsAfternoon;
	}

	public void setStartsAfternoon(boolean startsAfternoon) {
		this.startsAfternoon = startsAfternoon;
	}

	public LocalDateTime getLastDay() {
		return lastDay;
	}

	public void setLastDay(LocalDateTime lastDay) {
		this.lastDay = lastDay;
	}

	public boolean isEndsAfternoon() {
		return endsAfternoon;
	}

	public void setEndsAfternoon(boolean endsAfternoon) {
		this.endsAfternoon = endsAfternoon;
	}

	public double getDays() {
		return days;
	}

	public void setDays(double days) {
		this.days = days;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	

}
