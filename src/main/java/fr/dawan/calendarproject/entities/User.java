package fr.dawan.calendarproject.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, length = 350)
	private String name;
	
	public enum EmployeeType {
		FORMATEUR, COMMERCIAL, ADMINISTRATIF, INDEPENDANT
	}
	
	@Enumerated(EnumType.STRING)
	private EmployeeType type;
	
	@Version
	private int version;
	
	//@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	//private List<Intervention> interventions = new ArrayList<Intervention>();
	
	//Constructor important pour la sérialization (exemple Jackson)
	public Employee() {
		
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EmployeeType getType() {
		return type;
	}

	public void setType(EmployeeType type) {
		this.type = type;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
}
