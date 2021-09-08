package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;

import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.entities.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

	Location locationDtoToLocation(LocationDto location);

	LocationDto locationToLocationDto(Location location);
}
