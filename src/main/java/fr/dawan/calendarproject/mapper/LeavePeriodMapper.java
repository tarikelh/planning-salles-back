package fr.dawan.calendarproject.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.LeavePeriodDg2Dto;
import fr.dawan.calendarproject.dto.LeavePeriodDto;
import fr.dawan.calendarproject.entities.LeavePeriod;

@Mapper(componentModel = "spring")
public interface LeavePeriodMapper {

	@Mapping(target = "employeeId", source = "user.employeeIdDg2")
	@Mapping(target = "employeeFullName", source = "user.fullname")
	LeavePeriodDto leavePeriodToLeavePeriodDto(LeavePeriod leavePeriod);

	@Mapping(target = "user", ignore = true)
	LeavePeriod leavePeriodDtoToLeavePeriod(LeavePeriodDto leavePeriodDto);

	@Mapping(target = "user", ignore = true)
	@Mapping(target = "firstDay", source = "firstDay")
	@Mapping(target = "lastDay", source = "lastDay")
	LeavePeriod leavePeriodDg2DtoToLeavePeriod(LeavePeriodDg2Dto leavePeriodDg2Dto);
	
	@Mapping(target = "employeeId", source = "user.employeeIdDg2")
	@Mapping(target = "employeeFullName", source = "user.fullname")
	List<LeavePeriodDto> listLeavePeriodToListLeavePeriodDto(List<LeavePeriod> leavePeriods);

	@Mapping(target = "user", ignore = true)
	List<LeavePeriod> listLeavePeriodDtoToListLeavePeriod(List<LeavePeriodDto> leavePeriodsDtos);
}
