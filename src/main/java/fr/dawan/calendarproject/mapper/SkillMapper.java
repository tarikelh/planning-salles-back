package fr.dawan.calendarproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.SkillDto;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.repositories.SkillRepository;

@Mapper(componentModel = "spring", uses = { SkillRepository.class, UserMapper.class })
public interface SkillMapper {


	@Mapping(source = "users", target = "usersId")
	SkillDto skillToAdvancedSkillDto(Skill skill);

	@Mapping(source = "usersId", target = "users")
	Skill advancedSkillDtoToSkill(SkillDto advSkill);
}