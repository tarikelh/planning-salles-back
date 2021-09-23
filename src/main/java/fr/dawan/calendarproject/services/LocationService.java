package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.LocationDto;

public interface LocationService {

	List<LocationDto> getAllLocations();

	List<LocationDto> getAllLocations(int page, int max);

	LocationDto getById(long id);

	void deleteById(long id);

	LocationDto saveOrUpdate(LocationDto location);

	LocationDto count();
	
	boolean checkUniqness(LocationDto location);
	
	List<LocationDto> fetchAllDG2Locations() throws Exception;

}
