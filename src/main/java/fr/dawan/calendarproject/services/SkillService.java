package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.AvancedSkillDto;

public interface SkillService {

	List<AvancedSkillDto> getAllSkills();

	List<AvancedSkillDto> getAllSkills(int page, int max);

	AvancedSkillDto getById(long id);

	void deleteById(long id);

	AvancedSkillDto saveOrUpdate(AvancedSkillDto skill);

	long count();

}
