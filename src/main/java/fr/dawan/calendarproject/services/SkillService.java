package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.SkillDto;

public interface SkillService {

	List<SkillDto> getAllSkills();

	List<SkillDto> getAllSkills(int page, int size, String search);

	CountDto count(String search);

	SkillDto getById(long id);

	void deleteById(long id);

	SkillDto saveOrUpdate(SkillDto skill);

	boolean checkIntegrity(SkillDto s);
}
