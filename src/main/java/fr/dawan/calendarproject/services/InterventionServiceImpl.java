package fr.dawan.calendarproject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.InterventionCaretaker;
import fr.dawan.calendarproject.repositories.InterventionRepository;

@Service
@Transactional
public class InterventionServiceImpl implements InterventionService {

	@Autowired
	InterventionRepository interventionRepository;
	

	@Override
	public List<InterventionDto> getAllInterventions() {
		List<Intervention> interventions = interventionRepository.findAll();
		List<InterventionDto> interventionsDto = new ArrayList<InterventionDto>();

		for (Intervention intervention : interventions) {
			interventionsDto.add(DtoTools.convert(intervention, InterventionDto.class));
		}
		return interventionsDto;
	}

	@Override
	public List<InterventionDto> getAllInterventions(int page, int max) {
		List<Intervention> interventions = interventionRepository.findAll(PageRequest.of(page, max)).get()
				.collect(Collectors.toList());
		List<InterventionDto> interventionsDto = new ArrayList<InterventionDto>();
		for (Intervention intervention : interventions) {
			interventionsDto.add(DtoTools.convert(intervention, InterventionDto.class));
		}
		return interventionsDto;
	}

	@Override
	public InterventionDto getById(long id) {
		Optional<Intervention> intervention = interventionRepository.findById(id);
		if (intervention.isPresent())
			return DtoTools.convert(intervention.get(), InterventionDto.class);
		return null;
	}

	@Override
	public void deleteById(long id) {
		interventionRepository.deleteById(id);
	}

	@Override
	public InterventionDto saveOrUpdate(InterventionDto intervention) {
		interventionRepository.save(DtoTools.convert(intervention, Intervention.class));
		return intervention;
	}

	// Search
	@Override
	public List<InterventionDto> getByCourseId(long id) {
		List<Intervention> interventions = interventionRepository.findByCourseId(id);
		List<InterventionDto> iDtos = new ArrayList<InterventionDto>();

		for (Intervention i : interventions)
			iDtos.add(DtoTools.convert(i, InterventionDto.class));

		return iDtos;
	}

	// Search
	@Override
	public List<InterventionDto> getByCourseTitle(String title) {
		List<Intervention> interventions = interventionRepository.findByCourseTitle(title);
		List<InterventionDto> iDtos = new ArrayList<InterventionDto>();

		for (Intervention i : interventions)
			iDtos.add(DtoTools.convert(i, InterventionDto.class));

		return iDtos;
	}

	@Override
	public long count() {
		return interventionRepository.count();
	}
}
