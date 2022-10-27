package fr.dawan.calendarproject.services;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.LeavePeriodDg2Dto;
import fr.dawan.calendarproject.dto.LeavePeriodDto;
import fr.dawan.calendarproject.entities.LeavePeriod;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.mapper.LeavePeriodMapper;
import fr.dawan.calendarproject.repositories.LeavePeriodRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@Service
@Transactional
public class LeavePeriodServiceImpl implements LeavePeriodService {

	@Autowired
	private LeavePeriodRepository leavePeriodRepository;

	@Autowired
	private LeavePeriodMapper leavePeriodMapper;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private UserRepository userRepository;

	/**
	 * Fetches all of the existing leave periods.
	 * 
	 * @return result Returns a list of leave periods.
	 *
	 */
	
	@Override
	public List<LeavePeriodDto> getAllLeavePeriods() {

		return leavePeriodMapper.listLeavePeriodToListLeavePeriodDto(leavePeriodRepository.findAll());
	}

	/**
	 * Fetches all of the existing leave periods for a single user.
	 * 
	 * @param id An unique Integer used to identify each the leave periods
	 *               involving a specific user.
	 * 
	 * @return List<LeavePeriodDto> Returns a list of leave periods.
	 *
	 */
	
	@Override
	public List<LeavePeriodDto> getAllLeavePeriodsByEmployeeId(long id) {

		return leavePeriodMapper.listLeavePeriodToListLeavePeriodDto(leavePeriodRepository.findByUserEmployeeId(id));
	}
	
	/**
	 * Counts the number of leave periods for a specific type of user.
	 * 
	 * @param type A String representing the user's type to search for a
	 *               specific leave period.
	 * 
	 * @return CountDto Returns the number of leave periods, according to the type
	 *         of user.
	 *
	 */
	
	@Override
	public CountDto count(String type) {
		CountDto countDto;
		if (UserType.contains(type)) {
			countDto = new CountDto(leavePeriodRepository.countByUserType(UserType.valueOf(type)));
		}
		else {
			countDto = null;
		}
		return countDto;
	}

	/**
	 * Fetches all leave periods in the Dawan API for an interval [firstDay, lastDay].
	 * 
	 * @param email A String defining a user's email.
	 * @param pwd   A String defining a user's password.
	 * @param firstDay A String defining the first day of the interval.
	 * @param lastDay   A String defining the last day the interval.
	 * 
	 * @exception Exception Returns an exception if the request fails.
	 * 
	 * @return count An integer the number of leave periods 
	 * 			fetched form Dawan API if there isn't an exception
	 *
	 */
	
	@Override
	public int fetchAllDG2LeavePeriods(String email, String password, String firstDay, String lastDay)
			throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		List<LeavePeriodDg2Dto> lResJson;
		int count = 0;

		String uri = "https://dawan.org/api2/planning/leave-periods/" + firstDay + "/" + lastDay;
		URI url = new URI(uri);

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-AUTH-TOKEN", email + ":" + password);

		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		ResponseEntity<String> repWs = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

		if (repWs.getStatusCode() == HttpStatus.OK) {
			String json = repWs.getBody();
			LeavePeriodDg2Dto[] resArray = objectMapper.readValue(json, LeavePeriodDg2Dto[].class);
			lResJson = Arrays.asList(resArray);
			for (LeavePeriodDg2Dto lpDg2 : lResJson) {
				lpDg2.setFirstDay(lpDg2.getFirstDay().split("T")[0]);
				lpDg2.setLastDay(lpDg2.getLastDay().split("T")[0]);
				
				
				LeavePeriod leavePeriodImport = leavePeriodMapper.leavePeriodDg2DtoToLeavePeriod(lpDg2);
				
				//if check is done on idDg2 => set slug (1)
				//if check is done on slug => set idDg2 (2)
/*(1)*/			LeavePeriod alreadyInDb = leavePeriodRepository.findByIdDg2(leavePeriodImport.getIdDg2()).orElse(null);
//(2)			LeavePeriod alreadyInDb = leavePeriodRepository.findBySlug(leavePeriodImport.getSlug()).orElse(null);				
				if(alreadyInDb != null) {
					leavePeriodImport.setId(alreadyInDb.getId());
/*(1)*/				leavePeriodImport.setSlug(alreadyInDb.getSlug());
//(2)				leavePeriodImport.setIdDg2(alreadyInDb.getIdDg2());
					leavePeriodImport.setVersion(alreadyInDb.getVersion());
				}
						
				count++;
				try {
					leavePeriodImport.setUser(userRepository.findByEmployeeIdDg2(lpDg2.getEmployeeId()).orElse(null));
					leavePeriodRepository.saveAndFlush(leavePeriodImport);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return count;
	}

	/**
	 * Fetches all leave periods for an type of user and an interval [dateStart, dateEnd].
	 * 
	 * @param type		A String defining the type of user.
	 * @param dateStart A LocalDate defining the start day of the leave period.
	 * @param dateEnd   A LocalDate defining the end day of the leave period.
	 * 
	 * @return List<LeavePeriodDto> Returns a list of leave periods
	 *
	 */
	
	@Override
	public List<LeavePeriodDto> getBetween(String type, LocalDate dateStart, LocalDate dateEnd) {
		List<LeavePeriodDto> iDtos = new ArrayList<>();
		if (UserType.contains(type)) {
			UserType userType = UserType.valueOf(type);
			List<LeavePeriod> leavePeriods = leavePeriodRepository.getAllByUserTypeAndDates(userType, dateStart,
				dateEnd);
			iDtos = leavePeriodMapper.listLeavePeriodToListLeavePeriodDto(leavePeriods);
		}
		return iDtos;
	}
}
