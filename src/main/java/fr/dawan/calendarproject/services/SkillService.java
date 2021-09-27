package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.AdvancedSkillDto;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.entities.Skill;

public interface SkillService {

	List<AdvancedSkillDto> getAllSkills(int page, int size, String search);
	
	CountDto count(String search);

	AdvancedSkillDto getById(long id);

	void deleteById(long id);

	AdvancedSkillDto saveOrUpdate(AdvancedSkillDto skill);

	boolean checkIntegrity(AdvancedSkillDto s);
	
	Skill getEntityById(long id);
	
	Long getLongId(Skill skill);
}
