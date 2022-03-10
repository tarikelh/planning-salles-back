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

	@Mapping(source= ".", target = "title")
	@Mapping(target="id", ignore = true)
	@Mapping(target="users", ignore = true)
	@Mapping(target="version", ignore = true)
	Skill StringToSkill(String skill);
	
	default String skillToString(Skill skill) {
		return skill.getTitle();
	}

	Set<Skill> listSkillsToSetSkills(List<Skill> skils);

	Set<Skill> listSkillDtoToSetSkills(List<String> skills);

	@Mapping(source = "users", target = "usersId")
	AdvancedSkillDto skillToAdvancedSkillDto(Skill skill);

	@Mapping(source = "usersId", target = "users")
	Skill advancedSkillDtoToSkill(AdvancedSkillDto advSkill);
}