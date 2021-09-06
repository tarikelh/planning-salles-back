package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.entities.Location;

public interface LocationService {

	List<LocationDto> getAllLocations();

	List<LocationDto> getAllLocations(int page, int max);

	LocationDto getById(long id);

	void deleteById(long id);

	LocationDto saveOrUpdate(LocationDto location);

	boolean checkUniqness(LocationDto location);

	Location findById(long id);

	Long getEntityById(Location location);
}
