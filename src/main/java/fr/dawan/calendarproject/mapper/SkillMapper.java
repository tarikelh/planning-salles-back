package fr.dawan.calendarproject.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.SkillDto;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.repositories.SkillRepository;

@Mapper(componentModel = "spring", uses = { SkillRepository.class, UserMapper.class })
public interface SkillMapper {


	@Mapping(source = "users", target = "usersId")
	SkillDto skillToSkillDto(Skill skill);

	@Mapping(source = "usersId", target = "users")
	Skill skillDtoToSkill(SkillDto advSkill);
	
	Set<Skill> skillDtoListToSkillSet(List<SkillDto> listSkillDto);
	
	List<SkillDto> skillSetToSkillDtoList(Set<Skill> skills);
}