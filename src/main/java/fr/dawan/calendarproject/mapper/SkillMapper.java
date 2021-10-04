package fr.dawan.calendarproject.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.AdvancedSkillDto;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.repositories.SkillRepository;

@Mapper(componentModel = "spring", uses = { SkillRepository.class, UserMapper.class })
public interface SkillMapper {

	List<Long> setSkillsToListLong(Set<Skill> skills);

	Set<Skill> listSkillToSetSkill(List<Skill> skils);

	default Long getSkillId(Skill skill) {
		return skill.getId();
	}

	Set<Skill> listLongToSetSkills(List<Long> ids);

	@Mapping(source = "users", target = "usersId")
	AdvancedSkillDto skillToAdvancedSkillDto(Skill skill);

	@Mapping(source = "usersId", target = "users")
	Skill AdvancedSkillDtoToSkill(AdvancedSkillDto advSkill);
}