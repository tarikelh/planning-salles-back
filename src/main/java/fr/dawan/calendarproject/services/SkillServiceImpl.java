package fr.dawan.calendarproject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.AvancedSkillDto;
import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.repositories.SkillRepository;

@Service
@Transactional
public class SkillServiceImpl implements SkillService {

	@Autowired
	SkillRepository skillRepository;

	@Override
	public List<AvancedSkillDto> getAllSkills() {
		List<Skill> skills = skillRepository.findAll();
		List<AvancedSkillDto> result = new ArrayList<AvancedSkillDto>();

		for (Skill s : skills) {
			result.add(DtoTools.convert(s, AvancedSkillDto.class));
		}

		return result;
	}

	@Override
	public List<AvancedSkillDto> getAllSkills(int page, int max) {
		List<Skill> skills = skillRepository.findAll(PageRequest.of(page, max)).get().collect(Collectors.toList());
		List<AvancedSkillDto> result = new ArrayList<AvancedSkillDto>();

		for (Skill s : skills) {
			result.add(DtoTools.convert(s, AvancedSkillDto.class));
		}
		return result;
	}

	@Override
	public AvancedSkillDto getById(long id) {
		Optional<Skill> skill = skillRepository.findById(id);
		return DtoTools.convert(skill.get(), AvancedSkillDto.class);
	}

	@Override
	public void deleteById(long id) {
		skillRepository.deleteById(id);
	}

	@Override
	public AvancedSkillDto saveOrUpdate(AvancedSkillDto skill) {
		Skill s = DtoTools.convert(skill, Skill.class);
		s = skillRepository.saveAndFlush(s);
		return DtoTools.convert(s, AvancedSkillDto.class);
	}

	@Override
	public long count() {
		return skillRepository.count();
	}
}
