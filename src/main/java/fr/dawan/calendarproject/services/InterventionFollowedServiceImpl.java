package fr.dawan.calendarproject.services;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.InterventionFollowedDG2Dto;
import fr.dawan.calendarproject.dto.InterventionFollowedDto;
import fr.dawan.calendarproject.entities.InterventionFollowed;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.mapper.InterventionFollowedMapper;
import fr.dawan.calendarproject.repositories.InterventionFollowedRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@Service
@Transactional
public class InterventionFollowedServiceImpl implements InterventionFollowedService {

	@Autowired
	private InterventionFollowedRepository intervFolloRepository;

	@Autowired
	private InterventionFollowedMapper intervFolloMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private InterventionRepository interventionRepository;

	@Autowired
	RestTemplate restTemplate;

	/**
	 * Fetches all of the existing interventions Followed.
	 * 
	 * @return InterventionsFollowedDto Returns a list of interventions Followed.
	 *
	 */
	@Override
	public List<InterventionFollowedDto> getAllInterventionsFollowed() {

		return intervFolloMapper.listInterventionFollowedToListInterventionFollowedDto(intervFolloRepository.findAll());
	}

	/**
	 * Fetches all of the existing interventions Followed, with a pagination system.
	 * 
	 * @param page An integer representing the current page displaying the
	 *             interventions Followed.
	 * @param max An integer representing the last page of the pagination results.
	 * 
	 * @return interventionsFollowedDto Returns a list of interventions Followed, according to the
	 *         pagination criteria.
	 *
	 */
	@Override
	public List<InterventionFollowedDto> getAllInterventionsFollowed(int page, int max) {
		List<InterventionFollowed> interventionsFoll = intervFolloRepository.findAll(PageRequest.of(page, max)).get()
				.collect(Collectors.toList());
		return intervFolloMapper.listInterventionFollowedToListInterventionFollowedDto(interventionsFoll);
	}

	@Override
	public CountDto count() {
		return new CountDto(intervFolloRepository.count());
	}

	/**
	 * Fetches a single intervention Followed, according to its id.
	 * 
	 * @param id An unique Integer used to identify each intervention Followed.
	 * 
	 * @return InterventionFollowedDto Returns a single intervention Followed.
	 *
	 */
	@Override
	public InterventionFollowedDto getById(long id) {
		return intervFolloMapper
				.interventionFollowedToInterventionFollowedDto(intervFolloRepository.findById(id).orElse(null));
	}

	/**
	 * Delete a single intervention Followed, according to its id.
	 * 
	 * @param id An unique Integer used to identify each intervention Followed.
	 *
	 */
	@Override
	public void deleteById(long id) {
		intervFolloRepository.deleteById(id);

	}

	/**
	 * Adds a new intervention Followed or update an existing one.
	 * 
	 * @param interventionFollowed An object representing an Intervention.
	 * 
	 * @return InterventionFollowedDto Returns the newly created Intervention or an updated
	 *         one.
	 *
	 */
	@Override
	public InterventionFollowedDto saveOrUpdate(InterventionFollowedDto interventionsFollowed) {
		InterventionFollowedDto intervFollDto = null;
		if (interventionsFollowed.getId() <= 0
				|| intervFolloRepository.findById(interventionsFollowed.getId()).isPresent()) {
			InterventionFollowed i = intervFolloMapper
					.interventionFollowedDtoToInterventionFollowed(interventionsFollowed);

			try {
				i = intervFolloRepository.saveAndFlush(i);
			} catch (Exception e) {
				e.printStackTrace();
			}
			intervFollDto = intervFolloMapper.interventionFollowedToInterventionFollowedDto(i);
		}
		return intervFollDto;
	}

	/**
	 * Fetches all of the existing interventions Followed with a specific user type.
	 * 
	 * @param type A UserType referring to the type of user.
	 * 
	 * @return Returns a list of InterventionFollowedDto according to the user type.
	 *
	 */
	@Override
	public List<InterventionFollowedDto> findAllByUserType(UserType type) {
		return intervFolloMapper.listInterventionFollowedToListInterventionFollowedDto(intervFolloRepository.getAllByUserType(type));
	}
	
	/**
	 * Fetches all of the existing interventions Followed between two dates and with a specific user type.
	 * 
	 * @param type  A UserType referring to the type of user.
	 * @param start A LocalDate referring to the starting date.
	 * @param end   A LocalDate referring to the end date.
	 * 
	 * @return Returns a list of InterventionFollowedDto.
	 *
	 */
	@Override
	public List<InterventionFollowedDto> findAllByUserTypeAndDateRange(UserType type, LocalDate start, LocalDate end) {
		List<InterventionFollowed> lstIntervFollo = intervFolloRepository.getAllByUserTypeAndDateRange(type, start, end);
		return intervFolloMapper.listInterventionFollowedToListInterventionFollowedDto(lstIntervFollo);
	}

	/**
	 * Fetches all of the existing interventions Followed between two dates.
	 * 
	 * @param start A LocalDate referring to the starting date.
	 * @param end   A LocalDate referring to the end date.
	 * 
	 * @return Returns a list of InterventionFollowedDto.
	 *
	 */
	@Override
	public List<InterventionFollowedDto> findAllByDateRange(LocalDate start, LocalDate end) {
		return intervFolloMapper.listInterventionFollowedToListInterventionFollowedDto(intervFolloRepository.findAllByDateRange(start, end));
	}

	/**
	 * Counts all interventions Followed in the Dawan API, between two specific dates.
	 * 
	 * @param email A String defining a user's email.
	 * @param pwd   A String defining a user's password.
	 * @param start A LocalDate referring to the starting date of an intervention.
	 * @param end   A LocalDate referring to the end date of intervention.
	 * 
	 * @return count Returns the number of interventions of the Dawan API between
	 *         two dates.
	 * 
	 * @exception Exception Returns an exception if the request fails.
	 *
	 */
	@Override
	public int fetchAllDG2InterventionsFollowed(String email, String pwd, LocalDate start, LocalDate end)
			throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		List<InterventionFollowedDG2Dto> lResJson;
		List<InterventionFollowed> intervsFollToSave = new ArrayList<>();
		int count = 0;

		URI url = new URI(String.format("https://dawan.org/api2/planning/trainings-followed/by-date/" + start.toString()
				+ "/" + end.toString()));

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-AUTH-TOKEN", email + ":" + pwd);

		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		ResponseEntity<String> repWs = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

		if (repWs.getStatusCode() == HttpStatus.OK) {
			String json = repWs.getBody();
			InterventionFollowedDG2Dto[] resArray = objectMapper.readValue(json, InterventionFollowedDG2Dto[].class);

			lResJson = Arrays.asList(resArray);

			for (InterventionFollowedDG2Dto iFDG2 : lResJson) {
				InterventionFollowed intervsFoll = intervFolloMapper
						.interventionFollowedDG2DtoToInterventionFollowed(iFDG2);
				intervsFoll.setIntervention(
						interventionRepository.findByIdDg2WithOutOption(iFDG2.getInterventionId()).orElse(null));
				intervsFoll.setStudent(userRepository.findByIdDg2(iFDG2.getPersonId()).orElse(null));

				if (intervsFoll != null && intervsFoll.getIntervention() != null && intervsFoll.getStudent() != null) {

					InterventionFollowed alreadyInDb = intervFolloRepository
							.findByRegistrationSlug(iFDG2.getRegistrationSlug()).orElse(null);

					if (alreadyInDb != null && !alreadyInDb.equals(intervsFoll)) {

						intervsFoll.setId(alreadyInDb.getId());
						intervsFoll.setVersion(alreadyInDb.getVersion());
						intervsFollToSave.add(intervsFoll);
						count++;

					} else if (alreadyInDb == null) {
						intervsFollToSave.add(intervsFoll);
						count++;
					}

				}

			}
		} else {
			throw new Exception("ResponseEntity from the webservice WDG2 not correct");
		}

		try {
			intervsFollToSave = intervFolloRepository.saveAll(intervsFollToSave);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;

	}

}
