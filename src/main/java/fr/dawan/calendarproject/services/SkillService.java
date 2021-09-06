package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.AdvancedSkillDto;
import fr.dawan.calendarproject.entities.Skill;

public interface SkillService {

	List<AdvancedSkillDto> getAllSkills();

	List<AdvancedSkillDto> getAllSkills(int page, int max);

	AdvancedSkillDto getById(long id);

	void deleteById(long id);

	AdvancedSkillDto saveOrUpdate(AdvancedSkillDto skill);

	long count();

	boolean checkIntegrity(AdvancedSkillDto s);

	Skill findById(long id);

	Long getEntityById(Skill skill);
}
