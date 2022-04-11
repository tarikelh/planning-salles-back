package fr.dawan.calendarproject.mapper;

import fr.dawan.calendarproject.dto.CongeDg2Dto;
import fr.dawan.calendarproject.dto.CongeDto;
import fr.dawan.calendarproject.entities.Conge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CongeMapper {

	@Mapping(target = "id", source = "idDg2")
	CongeDto congeToCongeDto(Conge conge);

	@Mapping(target = "idDg2", source = "id")
	Conge congeDtoToConge(CongeDto congeDto);

	@Mapping(target = "idDg2", source = "id")
	Conge congeDg2DtoToConge(CongeDg2Dto congeDg2Dto);

}
