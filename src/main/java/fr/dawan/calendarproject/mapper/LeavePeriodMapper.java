package fr.dawan.calendarproject.mapper;

import fr.dawan.calendarproject.dto.LeavePeriodDg2Dto;
import fr.dawan.calendarproject.dto.LeavePeriodDto;
import fr.dawan.calendarproject.entities.LeavePeriod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LeavePeriodMapper {

	@Mapping(target = "id", source = "idDg2")
	LeavePeriodDto leavePeriodToLeavePeriodDto(LeavePeriod leavePeriod);

	@Mapping(target = "idDg2", source = "id")
	LeavePeriod leavePeriodDtoToLeavePeriod(LeavePeriodDto leavePeriodDto);

	@Mapping(target = "idDg2", source = "id")
	@Mapping(target = "firstDay", source = "firstDay", qualifiedByName="dateTimeToDateTime")
	@Mapping(target = "lastDay", source = "lastDay", qualifiedByName="dateTimeToDateTime")
	LeavePeriod leavePeriodDg2DtoToLeavePeriod(LeavePeriodDg2Dto leavePeriodDg2Dto);

	
	@Named("dateTimeToDateTime")
	public static LocalDateTime dateTimeToDateTime(String dateString) {
		
		String formated = dateString.substring(0,10) + " " + dateString.substring(11,19);
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.parse(formated, pattern);
		
	}

}
