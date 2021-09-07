package fr.dawan.calendarproject.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.services.SkillServiceImpl;

@Mapper(componentModel = "spring", uses = { SkillServiceImpl.class })
public interface SkillMapper {

	List<Long> setSkillsToListLong(Set<Skill> skills);
	
	Long skillToLong(Skill skill);

	Set<Skill> listLongToSetSkills(List<Long> ids);
}
