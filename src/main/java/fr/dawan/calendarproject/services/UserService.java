package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.EmployeeDto;

public interface EmployeeService {

	List<EmployeeDto> getAllEmployees();
	List<EmployeeDto> getAllEmployees(int page, int max);
	EmployeeDto getById(long id);
	void deleteById(long id);
	EmployeeDto saveOrUpdate(EmployeeDto employee);
	long count();
	
}
