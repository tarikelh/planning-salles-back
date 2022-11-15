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

import fr.dawan.calendarproject.enums.LeavePeriodType;

@Entity
public class LeavePeriod {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(cascade = CascadeType.MERGE)
	private User user;

	private String slug;

	@Enumerated(EnumType.STRING)
	private LeavePeriodType type;

	private LocalDate firstDay;
	private boolean startsAfternoon;
	private LocalDate lastDay;
	private boolean endsAfternoon;
	private double days;
	
	@Column(nullable = true, columnDefinition = "TEXT")
	private String comments;
	
	public LeavePeriod() {
		super();
	}

	public LeavePeriod(long id, User user, String slug, LeavePeriodType type, LocalDate firstDay,
			boolean startsAfternoon, LocalDate lastDay, boolean endsAfternoon, double days, String comments) {
		super();
		this.id = id;
		this.user = user;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public LeavePeriodType getType() {
		return type;
	}

	public void setType(LeavePeriodType type) {
		this.type = type;
	}

	public LocalDate getFirstDay() {
		return firstDay;
	}

	public void setFirstDay(LocalDate firstDay) {
		this.firstDay = firstDay;
	}

	public boolean isStartsAfternoon() {
		return startsAfternoon;
	}

	public void setStartsAfternoon(boolean startsAfternoon) {
		this.startsAfternoon = startsAfternoon;
	}

	public LocalDate getLastDay() {
		return lastDay;
	}

	public void setLastDay(LocalDate lastDay) {
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
