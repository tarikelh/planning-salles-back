package fr.dawan.calendarproject.dto;

import fr.dawan.calendarproject.entities.Employee.EmployeeType;

public class EmployeeDto {

	private long id;

	private String name;

	private EmployeeType type;
	
	private int version;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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
}
