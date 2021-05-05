package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.AdvancedSkillDto;

public interface SkillService {

	List<AdvancedSkillDto> getAllSkills();

	List<AdvancedSkillDto> getAllSkills(int page, int max);

	AdvancedSkillDto getById(long id);

	void deleteById(long id);

	AdvancedSkillDto saveOrUpdate(AdvancedSkillDto skill);

	long count();

}
