package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.CenterDto;
import fr.dawan.calendarproject.entities.Center;

@Mapper(componentModel = "spring")
public interface CenterMapper {
	
	@Mapping(source = "location.id", target = "locationId")
	CenterDto centerToCenterDto(Center center);
	
	@Mapping(source = "locationId", target = "location.id")
	Center centerDtoToCenter(CenterDto centerDto);
}
