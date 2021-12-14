package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.LocationDG2Dto;
import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.entities.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

	Location locationDtoToLocation(LocationDto location);

	LocationDto locationToLocationDto(Location location);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "city", source = "name")
	@Mapping(target = "color", ignore = true)
	@Mapping(target = "idDg2", source = "id")
	@Mapping(target = "version", ignore = true)
	Location locationDG2DtoToLocation(LocationDG2Dto locationDG2Dto);

}
