package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.ResourceDto;

public interface ResourceService {

	List<ResourceDto> getAllResources();
	
	List<ResourceDto> getAllResources(int page, int max, String search);
	
	List<ResourceDto> getResourceByRoomAvailability(boolean  availability);
	
	List<ResourceDto> getResourceByQuantity(int quantity);
	
	List<ResourceDto> getResourceByQuantityRange(int value1, int value2);
	
	ResourceDto getById(long id);
	
	CountDto count(String search);
	
	void deleteById(long id);
	
	ResourceDto saveOrUpdate(ResourceDto rDto);	

}
