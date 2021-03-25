package fr.dawan.calendarproject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.InterventionCaretaker;
import fr.dawan.calendarproject.entities.InterventionMemento;
import fr.dawan.calendarproject.repositories.CourseRepository;
import fr.dawan.calendarproject.repositories.InterventionMementoRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@Service
@Transactional
public class InterventionServiceImpl implements InterventionService {

	@Autowired
	private InterventionRepository interventionRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private InterventionCaretaker caretaker;

	@Autowired
	private InterventionMementoRepository interventionMementoRepository;

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
	public InterventionDto saveOrUpdate(InterventionDto intervention) throws Exception {
		Intervention interv = DtoTools.convert(intervention, Intervention.class);
		// construire objet interventionMemento
		InterventionMemento intMemento = new InterventionMemento();
		intMemento.setState(DtoTools.convert(interv, InterventionMementoDto.class));
		// sauvegarder le memento
		caretaker.addMemento(intervention.getId(), "test", intMemento.createMemento());
		interventionMementoRepository.saveAndFlush(intMemento);
		// List<InterventionMemento> testList = interventionMementoRepository.findAll();
		// pour récupérer la bonne version de chaque Objets appelés dans Intervention
		interv.setLocation(locationRepository.getOne(interv.getLocation().getId()));
		interv.setCourse(courseRepository.getOne(interv.getCourse().getId()));
		interv.setUser(userRepository.getOne(interv.getUser().getId()));
		if (interv.getId() != 0) {
			interv.setVersion(interventionRepository.getOne(interv.getId()).getVersion());
		}
		interventionRepository.saveAndFlush(interv);
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
