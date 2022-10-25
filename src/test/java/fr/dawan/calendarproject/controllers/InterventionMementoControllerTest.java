package fr.dawan.calendarproject.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.dto.MementoMessageDto;
import fr.dawan.calendarproject.entities.InterventionMemento;
import fr.dawan.calendarproject.interceptors.TokenInterceptor;
import fr.dawan.calendarproject.services.InterventionCaretaker;
import fr.dawan.calendarproject.tools.JwtTokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class InterventionMementoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private InterventionCaretaker caretaker;

	@MockBean
	private TokenInterceptor tokenInterceptor;
	
	@MockBean
	private JwtTokenUtil jwtTokenUtil;

	private List<InterventionMemento> mementos = new ArrayList<InterventionMemento>();
	private InterventionDto intDto;

	private String email = "admin@test.fr";

	@BeforeEach
	void beforeEach() throws Exception {
		when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);

		InterventionMementoDto mockedIntMemento = Mockito.mock(InterventionMementoDto.class);
//		intDto = new InterventionDto(1, 1, "lambdaSlug", "I am lambda Intervention", 0, 0, 0, 0, 0, 0, "SUR_MESURE", true, LocalDate.now(),
//				LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false, 0);

		mementos.add(new InterventionMemento(1, mockedIntMemento,
				new MementoMessageDto(1, " has been created by ", email, ""), 0));
		mementos.add(new InterventionMemento(2, mockedIntMemento,
				new MementoMessageDto(2, " has been created by ", email, ""), 0));
		mementos.add(new InterventionMemento(3, mockedIntMemento,
				new MementoMessageDto(3, " has been changed by ", email, ""), 0));
	}

	@Test
	void shouldFetchAllMementoPagination() throws Exception {
		when(caretaker.getAllMemento(any(int.class), any(int.class))).thenReturn(mementos);

		mockMvc.perform(get("/api/intervention-memento/{page}/{size}", 0, 3).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(mementos.size())))
				.andExpect(jsonPath("$[2].id", is(3)));
	}

	@Test
	void shouldFetchMementoById() throws Exception {
		final long mementoId = 2;
		when(caretaker.getMementoById(mementoId)).thenReturn(mementos.get(1));

		mockMvc.perform(get("/api/intervention-memento/{id}", mementoId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(2)));
	}

	@Test
	void shouldThrowErrorWhenIdDoesNotExist() throws Exception {
		final long mementoId = 6;
		when(caretaker.getMementoById(mementoId)).thenReturn(null);

		mockMvc.perform(get("/api/intervention-memento/{id}", mementoId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldFetchCountDto() throws Exception {
		CountDto countDto = new CountDto(3);
		when(caretaker.count()).thenReturn(countDto);

		mockMvc.perform(get("/api/intervention-memento/count").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.nb", is(3)));
	}

	@Test
	void shouldFilterMementosWithInterventionId() throws Exception {
		final long interventionId = 2;

		when(caretaker.filterMemento(any(long.class), any(LocalDateTime.class), any(LocalDateTime.class), any(int.class), any(int.class))).thenReturn(mementos);
		
		mockMvc.perform(
				get("/api/intervention-memento/filter/{interventionId}?start=2021-11-02 09:00&end=2021-11-05 12:00&page=0&size=3",
						interventionId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(mementos.size())))
				.andExpect(status().isOk()).andExpect(jsonPath("$[2].id", is(3)));
	}
	
	@Test
	void shouldThrowErrorFilterMementosEmpty() throws Exception {
		final long interventionId = 2;	
		List<InterventionMemento> mementosEmpty = new ArrayList<InterventionMemento>();

		when(caretaker.filterMemento(any(long.class), any(LocalDateTime.class), any(LocalDateTime.class), any(int.class), any(int.class))).thenReturn(mementosEmpty);
		
		mockMvc.perform(
				get("/api/intervention-memento/filter/{interventionId}?start=2021-11-02 09:00&end=2021-11-05 12:00&page=0&size=3",
						interventionId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldFetchCountDtoWithFilter() throws Exception {
		CountDto countDto = new CountDto(3);
		when(caretaker.countFilter(any(long.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(countDto);

		mockMvc.perform(get("/api/intervention-memento/count-filter?interventionId=2&start=2021-11-02 09:00&end=2021-11-05 12:00").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.nb", is(3)));
	}
	
	@Test
	void shouldRestoreMemento() throws Exception {
		final long mementoId = 2;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", "Bearer testTokenMemento");
		
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn(email);
		when(caretaker.restoreMemento(any(long.class), any(String.class))).thenReturn(intDto);
		
		mockMvc.perform(get("/api/intervention-memento/restore/{id}", mementoId).headers(httpHeaders).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.comment", is("I am lambda Intervention")));
	}
	
	@Test
	void shouldGetLastBeforeMemento() throws Exception {
		
		when(caretaker.getLastBeforeMemento(any(long.class), any(long.class))).thenReturn(mementos.get(1));
		
		mockMvc.perform(get("/api/intervention-memento/last-before?interventionId=2&interventionMementoId=3").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(2)));
	}
	
	@Test
	void shouldThrowErrorWhenNoLastBeforeMemento() throws Exception {
		
		when(caretaker.getLastBeforeMemento(any(long.class), any(long.class))).thenReturn(null);
		
		mockMvc.perform(get("/api/intervention-memento/last-before?interventionId=2&interventionMementoId=3").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldExportMementoCSV() throws Exception {
		doNothing().when(caretaker).serializeInterventionMementosAsCSV();

		MockHttpServletResponse response = mockMvc
				.perform(get("/api/intervention-memento/export-csv").accept("text/csv")).andExpect(status().isOk())
				.andExpect(content().contentType("text/csv")).andReturn().getResponse();

		assertEquals(response.getHeader("content-disposition"), "attachment; filename=\"interventionMemento.csv\"");
		assertEquals(response.getHeader("Cache-Control"), "no-cache, no-store, must-revalidate");
		assertEquals(response.getHeader("pragma"), "no-cache");
		assertEquals(response.getHeader("Expires"), "0");
	}

	@Test
	void shouldReturnErrorWhenCSVCreationFails() throws Exception {
		doThrow(Exception.class).when(caretaker).serializeInterventionMementosAsCSV();

		mockMvc.perform(get("/api/intervention-memento/export-csv").accept("text/csv"))
				.andExpect(status().isInternalServerError());
	}

	@Test
	void shouldExportMementoCSVByDates() throws Exception {
		doNothing().when(caretaker).serializeInterventionMementosAsCSVByDates(any(LocalDate.class),
				any(LocalDate.class));

		MockHttpServletResponse response = mockMvc
				.perform(get("/api/intervention-memento/export-csv-dates?dateStart=2021-08-10&dateEnd=2021-08-12")
						.accept("text/csv"))
				.andExpect(status().isOk()).andExpect(content().contentType("text/csv")).andReturn().getResponse();

		assertEquals(response.getHeader("content-disposition"),
				"attachment; filename=\"interventionMementoDates.csv\"");
		assertEquals(response.getHeader("Cache-Control"), "no-cache, no-store, must-revalidate");
		assertEquals(response.getHeader("pragma"), "no-cache");
		assertEquals(response.getHeader("Expires"), "0");
	}

	@Test
	void shouldReturnErrorWhenCSVCreationByDatesFails() throws Exception {
		doThrow(Exception.class).when(caretaker).serializeInterventionMementosAsCSVByDates(any(LocalDate.class),
				any(LocalDate.class));

		mockMvc.perform(get("/api/intervention-memento/export-csv-dates?dateStart=2021-08-10&dateEnd=2021-08-12")
				.accept("text/csv")).andExpect(status().isInternalServerError());
	}

}
