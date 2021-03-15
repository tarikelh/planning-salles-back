package fr.dawan.calendarproject.entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class Intervention {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	//dateDebut, dateFin, many interv to one Employe, many interv. to one
	@Column(columnDefinition = "dateStart")
	private LocalDate dateStart;
	
	//OU - @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(columnDefinition = "dateEnd")
	private LocalDate dateEnd;
	
	@Version
	private int version;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
    //@JoinColumn(name = "employee_id", nullable = false) //nom par defaut
    private Employee employee;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Course course;
	
	
	//Constructor important pour la sérialization (exemple Jackson)
	public Intervention() {
		
	}
	
	
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
}
