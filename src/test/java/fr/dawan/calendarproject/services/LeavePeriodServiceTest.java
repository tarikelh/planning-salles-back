package fr.dawan.calendarproject.services;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.LeavePeriodDg2Dto;
import fr.dawan.calendarproject.dto.LeavePeriodDto;
import fr.dawan.calendarproject.entities.LeavePeriod;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.LeavePeriodType;
import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.mapper.LeavePeriodMapper;
import fr.dawan.calendarproject.repositories.LeavePeriodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class LeavePeriodServiceTest {

	@Autowired
	private LeavePeriodService leavePeriodService;

	@MockBean
	private LeavePeriodRepository leavePeriodRepository;

	@MockBean
	private LeavePeriodMapper leavePeriodMapper;

	@MockBean
	private RestTemplate restTemplate;

	private List<LeavePeriod> lpList = new ArrayList<LeavePeriod>();
	private List<LeavePeriodDto> lpDtoList = new ArrayList<LeavePeriodDto>();
	private Optional<LeavePeriod> opLeavePeriod = null;

	@BeforeEach
	void beforeEach() throws Exception {

		Location loc = Mockito.mock(Location.class);

		LocalDate date = LocalDate.now();

		User user = new User(1L, 1L, 1L, "Daniel", "Balavoine", loc, "dbalavoine@dawan.fr", "testPassword", null,
				UserType.ADMINISTRATIF, UserCompany.DAWAN, "", date, null, 0);

		lpList.add(new LeavePeriod(1, 0, user, "Bla", LeavePeriodType.CP, date,true, date, false, 0.5, "No comments", 0));
		lpList.add(new LeavePeriod(2, 0, user, "Bla", LeavePeriodType.CP, date,true, date, false, 0.5, "No comments", 0));
		lpList.add(new LeavePeriod(3, 0, user, "Bla", LeavePeriodType.CP, date,true, date, false, 0.5, "No comments", 0));
		lpList.add(new LeavePeriod(4, 0, user, "Bla", LeavePeriodType.CP, date,true, date, false, 0.5, "No comments", 0));

		lpDtoList.add(new LeavePeriodDto(1, 1, 0, "Daniel Balavoine", "Bla", "CP",date.toString(), true, date.toString(), false, 0.5 , "No comments", 0));
		lpDtoList.add(new LeavePeriodDto(2, 2, 0, "Daniel Balavoine", "Bla", "CP",date.toString(), true, date.toString(), false, 0.5 , "No comments", 0));
		lpDtoList.add(new LeavePeriodDto(3, 3, 0, "Daniel Balavoine", "Bla", "CP",date.toString(), true, date.toString(), false, 0.5 , "No comments", 0));
		lpDtoList.add(new LeavePeriodDto(4, 4, 0, "Daniel Balavoine", "Bla", "CP",date.toString(), true, date.toString(), false, 0.5 , "No comments", 0));

		opLeavePeriod = Optional.of(lpList.get(0));
	}

	@Test
	void contextLoads() {
		assertThat(leavePeriodService).isNotNull();
	}

	@Test
	void shouldFetchAllLeavePeriodAndReturnDtos() {
		when(leavePeriodRepository.findAll()).thenReturn(lpList);
		when(leavePeriodMapper.leavePeriodToLeavePeriodDto(any(LeavePeriod.class))).thenReturn(lpDtoList.get(0), lpDtoList.get(1),
				lpDtoList.get(2), lpDtoList.get(3));

		List<LeavePeriodDto> result = leavePeriodService.getAllLeavePeriods();

		assertThat(result).isNotNull();
		assertEquals(lpDtoList.size(), result.size());
		assertEquals(lpDtoList, result);
	}

	@Test
	void shouldGetLeavePeriodByEmployeeId() {
		when(leavePeriodRepository.findByUserEmployeeId(any(long.class))).thenReturn(lpList);
		when(leavePeriodMapper.leavePeriodToLeavePeriodDto(any(LeavePeriod.class))).thenReturn(lpDtoList.get(1));

		LeavePeriodDto result = leavePeriodService.getAllLeavePeriodsByEmployeeId(1).get(1);

		assertThat(result).isNotNull();
		assertEquals(lpDtoList.get(1), result);
	}

	@Test
	void shouldGetLeavePeriodWithBetweenInterval() {
		when(leavePeriodRepository.getAllByUserTypeAndDates(any(UserType.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(lpList.subList(2,3));
		when(leavePeriodMapper.leavePeriodToLeavePeriodDto(any(LeavePeriod.class))).thenReturn(lpDtoList.get(2));

		List<LeavePeriodDto> result = leavePeriodService.getBetween("ADMINISTRATIF",Mockito.mock(LocalDate.class), Mockito.mock(LocalDate.class));

		assertThat(result).isNotNull();
		assertEquals(lpDtoList.subList(2,3), result);
		assertEquals(1, result.size());
	}


	@SuppressWarnings("unchecked")
	@Test
	void shouldGetLeavePeriodsFromDG2() throws Exception {
		String body = "[{\"id\":1,\"employeeId\":1,\"slug\":\"Bla\",\"type\":\"ADMINISTRATIF\",\"firstDay\":\"2022-04-01\",\"startsAfternoon\":true,\"lastDay\":\"2022-04-10\",\"endsAfternoon\":false,\"days\":0.5,\"comments\":\"No comments\"}]";
		ResponseEntity<String> res = new ResponseEntity<String>(body, HttpStatus.OK);
		when(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
				.thenReturn(res);
		when(leavePeriodMapper.leavePeriodDg2DtoToLeavePeriod(any(LeavePeriodDg2Dto.class))).thenReturn(lpList.get(0));
		when(leavePeriodRepository.findById(lpList.get(0).getId())).thenReturn(opLeavePeriod);
		when(leavePeriodRepository.saveAndFlush(lpList.get(0))).thenReturn(lpList.get(0));

		assertDoesNotThrow(() -> leavePeriodService.fetchAllDG2LeavePeriods("emailDG2", "passwordDG2","2022-04-01","2022-04-10"));
	}

}
