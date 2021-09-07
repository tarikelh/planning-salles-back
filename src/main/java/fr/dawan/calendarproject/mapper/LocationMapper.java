package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;

import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.services.LocationServiceImpl;

@Mapper(componentModel = "spring", uses = { LocationServiceImpl.class })
public interface LocationMapper {

	Location locationDtoToLocation(LocationDto location);

	LocationDto locationToLocationDto(Location location);
}
