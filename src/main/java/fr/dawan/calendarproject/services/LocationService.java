package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.LocationDto;

public interface LocationService {
	
	List<LocationDto> getAllLocations();

	List<LocationDto> getAllLocations(int page, int size, String search);

	LocationDto getById(long id);

	void deleteById(long id);

	LocationDto saveOrUpdate(LocationDto location);
	
	boolean checkUniqness(LocationDto location);
	
	void fetchAllDG2Locations() throws Exception;
	
	CountDto count(String search);

}
