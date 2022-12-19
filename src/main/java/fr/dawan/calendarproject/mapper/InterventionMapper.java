package fr.dawan.calendarproject.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import fr.dawan.calendarproject.dto.AdvancedInterventionDto;
import fr.dawan.calendarproject.dto.AdvancedInterventionDto2;
import fr.dawan.calendarproject.dto.CustomerDto;
import fr.dawan.calendarproject.dto.InterventionDG2Dto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.repositories.CourseRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@Mapper(componentModel = "spring", uses = { UserMapper.class, CourseRepository.class, LocationRepository.class,
        UserRepository.class, InterventionRepository.class })
public interface InterventionMapper {

    @Mapping(target = "locationIdDg2", source = "location.idDg2")
    @Mapping(target = "courseIdDg2", source = "course.idDg2")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "locationId", source = "location.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "masterInterventionId", source = "masterIntervention.id")
    InterventionDto interventionToInterventionDto(Intervention intervention);

    @Named("interventionToAdvInterventionDto")
    @Mapping(target = "user", source = "user", qualifiedByName = "userToUserDto")
    AdvancedInterventionDto interventionToAdvInterventionDto(Intervention intervention);

    @Mapping(qualifiedByName = "interventionToAdvInterventionDto", target = ".")
    List<AdvancedInterventionDto> listInterventionToListAdvInterventionDto(List<Intervention> interventions);

    @Mapping(target = "eventSiblings", ignore = true)
    AdvancedInterventionDto2 interventionToAdvInterventionDto2(Intervention intervention);

    List<AdvancedInterventionDto2> listInterventionToListAdvInterventionDto2(List<Intervention> interventions);

    @Mapping(target = "course", source = "courseId")
    @Mapping(target = "location", source = "locationId")
    @Mapping(target = "user", source = "userId")
    @Mapping(target = "masterIntervention", source = "masterInterventionId")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "masterInterventionIdTemp", ignore = true)
    @Mapping(target = "optionSlug", ignore = true)
    @Mapping(target = "interventionsFollowed", ignore = true)
    Intervention interventionDtoToIntervention(InterventionDto intervention);

    @Mapping(target = "interventionsFollowed", ignore = true)
    @Mapping(target = "masterInterventionIdTemp", ignore = true)
    Intervention advInterventionDtoToIntervention(AdvancedInterventionDto intervention);

    List<InterventionDto> listInterventionToListInterventionDto(List<Intervention> interventions);

    List<Intervention> listInterventionDtoToListIntervention(List<InterventionDto> interventionDtos);

    @Mapping(target = "idDg2", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", source = "type")
    @Mapping(target = "dateStart", source = "dateStart", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "dateEnd", source = "dateEnd", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "masterIntervention", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "comment", ignore = true)
    @Mapping(target = "timeStart", ignore = true)
    @Mapping(target = "timeEnd", ignore = true)
    @Mapping(target = "interventionsFollowed", ignore = true)
    @Mapping(target = "masterInterventionIdTemp", ignore = true)
    Intervention interventionDG2DtoToIntervention(InterventionDG2Dto iDG2);

    List<Intervention> listInterventionDG2DtotoListIntervention(List<InterventionDG2Dto> interventionDG2Dtos);

    default String listCustomerDtotoString(List<CustomerDto> customers) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getNb() > 1) {
                stringBuilder.append(customers.get(i).getNb());
                stringBuilder.append(" - ");
            }
            stringBuilder.append(customers.get(i).getCompanyName());
            if (i < customers.size() - 1)
                stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }
}
