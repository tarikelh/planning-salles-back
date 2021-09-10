package fr.dawan.calendarproject.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.AdvancedSkillDto;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.services.SkillService;

@Mapper(componentModel = "spring", uses = { SkillService.class, UserMapper.class })
public interface SkillMapper {

	List<Long> setSkillsToListLong(Set<Skill> skills);

	Long skillToLong(Skill skill);

	Set<Skill> listLongToSetSkills(List<Long> ids);

	@Mapping(source="users", target ="usersId")
	AdvancedSkillDto skillToAdvancedSkillDto(Skill skill);
}