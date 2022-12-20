package fr.dawan.calendarproject.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.LeavePeriodDto;
import fr.dawan.calendarproject.interceptors.TokenInterceptor;
import fr.dawan.calendarproject.services.LeavePeriodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LeavePeriodControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private LeavePeriodController leavePeriodController;

	@MockBean
	private LeavePeriodService leavePeriodService;

	@MockBean
	private TokenInterceptor tokenInterceptor;

	private List<LeavePeriodDto> leavePeriods = new ArrayList<LeavePeriodDto>();

	@BeforeEach()
	public void beforeEach() throws Exception {
		LocalDate date = LocalDate.now();

		when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);

		leavePeriods.add(new LeavePeriodDto(1, 1, 5, "Daniel Balavoine", "Bla1", "CP",date.toString(), true, date.toString(), false, 0.5 , "No comments", 0));
		leavePeriods.add(new LeavePeriodDto(2, 1, 5, "Daniel Balavoine", "Bla2", "CP",date.toString(), true, date.toString(), false, 0.5 , "No comments", 0));
		leavePeriods.add(new LeavePeriodDto(3, 1, 5, "Daniel Balavoine", "Bla3", "CP",date.toString(), true, date.toString(), false, 0.5 , "No comments", 0));
	}

	@Test
	void contextLoads() {
		assertThat(leavePeriodController).isNotNull();
	}

	@Test
	void shouldFetchAllLeavePeriods() throws Exception {
		when(leavePeriodService.getAllLeavePeriods()).thenReturn(leavePeriods);

		mockMvc.perform(get("/api/leave-periods").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(leavePeriods.size()))).andExpect(jsonPath("$[0].slug", is("Bla1")));
	}

	@Test
	void shouldFetchLeavePeriodByEmployeeId() throws Exception {
		final long employeeId = 1;
		when(leavePeriodService.getAllLeavePeriodsByEmployeeId(employeeId)).thenReturn(leavePeriods);

		mockMvc.perform(get("/api/leave-periods/{employeeId}", employeeId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].slug", is("Bla1")));
	}

	@Test
	void shouldReturn404WhenGetByEmployeeId() throws Exception {
		final long employeeId = 10;
		when(leavePeriodService.getAllLeavePeriodsByEmployeeId(employeeId)).thenReturn(null);

		mockMvc.perform(get("/api/leave-periods/{employeeId}", employeeId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldFetchLeavePeriodWithBetween() throws Exception {
		final long employeeId = 1;
		when(leavePeriodService.getBetween(any(String.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(leavePeriods);

		mockMvc.perform(get("/api/leave-periods/between?type=CP&start=2022-05-17&end=2022-05-19", employeeId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].slug", is("Bla1")));
	}

	@Test
	void shouldFetchLeavePeriodFromDg2AndReturnOkStatus() throws Exception {
		LocalDate firstDay = LocalDate.now();
		LocalDate lastDay = LocalDate.now().plusDays(2);

		when(leavePeriodService.fetchAllDG2LeavePeriods(any(String.class), any(String.class), any(String.class),
				any(String.class))).thenReturn(3);

		mockMvc.perform(get("/api/leave-periods/dg2/{firstDay}/{lastDay}", firstDay.toString(), lastDay.toString())
						.accept(MediaType.APPLICATION_JSON).header("x-auth-token", "test@dawan.fr:testPassword"))
				.andExpect(status().isOk());
	}

}
