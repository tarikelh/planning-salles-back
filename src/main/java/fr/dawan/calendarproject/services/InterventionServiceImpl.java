package fr.dawan.calendarproject.services;

import java.time.LocalDate;
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
import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.InterventionCaretaker;
import fr.dawan.calendarproject.entities.InterventionMemento;
import fr.dawan.calendarproject.repositories.CourseRepository;
import fr.dawan.calendarproject.repositories.InterventionMementoRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.UserRepository;
import fr.dawan.calendarproject.tools.CsvToolsGeneric;

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

	// For InterventionMemento CSV - to move in InterventionMementoServiceImpl ?
	@Override
	public void getAllIntMementoCSV() throws Exception {
		CsvToolsGeneric.toCsv("interventionMemento.csv", caretaker.getAllMemento(), ";");
	}

	// For InterventionMemento CSV between two dates - to move in
	// InterventionMementoServiceImpl ?
	public void getAllIntMementoCSVDates(LocalDate dateStart, LocalDate dateEnd) throws Exception {
		CsvToolsGeneric.toCsv("interventionMementoDates.csv", caretaker.getAllMementoDates(dateStart, dateEnd), ";");
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
		interv.setLocation(locationRepository.getOne(intervention.getLocationId()));
		interv.setCourse(courseRepository.getOne(intervention.getCourseId()));
		interv.setUser(userRepository.getOne(intervention.getUserId()));

		if (intervention.getMasterInterventionId() > 0)
			interv.setMasterIntervention(interventionRepository.getOne(intervention.getMasterInterventionId()));

		if (interv.getId() != 0) {
			interv.setVersion(interventionRepository.getOne(interv.getId()).getVersion());
		}
		
		if (Intervention.checkIntegrity(interv)) {
			interv = interventionRepository.saveAndFlush(interv);
			
			// Memento creation
			// Build interventionMemento object
			InterventionMemento intMemento = new InterventionMemento();
			intMemento.setState(DtoTools.convert(interv, InterventionMementoDto.class));
			// Save memento
			try {
				caretaker.addMemento(intervention.getId(), "test", intMemento.createMemento());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			interventionMementoRepository.saveAndFlush(intMemento);
		}
		return DtoTools.convert(interv, InterventionDto.class);
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
