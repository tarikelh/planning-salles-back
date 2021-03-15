package fr.dawan.calendarproject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.dto.EmployeeDto;
import fr.dawan.calendarproject.entities.Employee;
import fr.dawan.calendarproject.repositories.EmployeeRepository;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Override
	public List<EmployeeDto> getAllEmployees() {
		List<Employee> employees = employeeRepository.findAll();
		List<EmployeeDto> result = new ArrayList<EmployeeDto>();
		
		for (Employee e : employees) {
			result.add(DtoTools.convert(e, EmployeeDto.class));
		}
		
		return result;
	}

	@Override
	public List<EmployeeDto> getAllEmployees(int page, int max) {
		List<Employee> employees = employeeRepository.findAll(PageRequest.of(page, max)).get().collect(Collectors.toList());
		List<EmployeeDto> result = new ArrayList<EmployeeDto>();
		
		for (Employee e : employees) {
			result.add(DtoTools.convert(e, EmployeeDto.class));
		}
		return result;
	}

	@Override
	public EmployeeDto getById(long id) {
		Optional<Employee> employee = employeeRepository.findById(id);
		return DtoTools.convert(employee.get(), EmployeeDto.class);
	}

	@Override
	public void deleteById(long id) {
		employeeRepository.deleteById(id);
	}

	@Override
	public EmployeeDto saveOrUpdate(EmployeeDto employee) {
		Employee e =  DtoTools.convert(employee, Employee.class);
		e = employeeRepository.saveAndFlush(e);
		return DtoTools.convert(e, EmployeeDto.class);
	}

	@Override
	public long count() {
		return employeeRepository.count();
	}

}
