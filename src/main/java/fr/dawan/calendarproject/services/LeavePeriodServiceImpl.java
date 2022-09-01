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

	@Override
	public List<LeavePeriodDto> getAllLeavePeriods() {
		List<LeavePeriod> leavePeriod = leavePeriodRepository.findAll();

		List<LeavePeriodDto> result = new ArrayList<>();

		for (LeavePeriod lp : leavePeriod) {
			result.add(leavePeriodMapper.leavePeriodToLeavePeriodDto(lp));
		}
		return result;
	}

	@Override
	public List<LeavePeriodDto> getAllLeavePeriodsByEmployeeId(long id) {
		List<LeavePeriod> leavePeriod = leavePeriodRepository.findByUserEmployeeId(id);

		List<LeavePeriodDto> result = new ArrayList<>();
		for (LeavePeriod lp : leavePeriod) {
			result.add(leavePeriodMapper.leavePeriodToLeavePeriodDto(lp));
		}

		return result;
	}
	
	@Override
	public CountDto count(String type) {
		if (UserType.contains(type)) {
			UserType userType = UserType.valueOf(type);
			return new CountDto(leavePeriodRepository.countByUserType(userType));
		}

		return null; // Exception
	}

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
				
				
				List<LeavePeriod> optLeavePeriod = leavePeriodRepository
						.findByUserEmployeeId(lpDg2.getEmployeeId());

				if (!optLeavePeriod.contains(leavePeriodImport)) {
					count++;
					try {
						leavePeriodImport.setUser(userRepository.findByEmployeeIdDg2(lpDg2.getEmployeeId()).orElse(null));
						leavePeriodRepository.saveAndFlush(leavePeriodImport);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return count;
	}

	@Override
	public List<LeavePeriodDto> getBetween(String type, LocalDate dateStart, LocalDate dateEnd) {
		List<LeavePeriodDto> iDtos = new ArrayList<>();
		if (UserType.contains(type)) {
			UserType userType = UserType.valueOf(type);
			List<LeavePeriod> leavePeriods = leavePeriodRepository.getAllByUserTypeAndDates(userType, dateStart,
					dateEnd);
			for (LeavePeriod i : leavePeriods)
				iDtos.add(leavePeriodMapper.leavePeriodToLeavePeriodDto(i));

			return iDtos;
		} else {
			return iDtos;
		}

	}
}
