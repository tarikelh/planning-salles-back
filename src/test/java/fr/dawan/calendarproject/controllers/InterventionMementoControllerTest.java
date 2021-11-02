package fr.dawan.calendarproject.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import fr.dawan.calendarproject.interceptors.TokenInterceptor;
import fr.dawan.calendarproject.services.InterventionCaretaker;

@SpringBootTest
@AutoConfigureMockMvc
public class InterventionMementoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private InterventionCaretaker caretaker;

	@MockBean
	private TokenInterceptor tokenInterceptor;

	@BeforeEach
	void beforeEach() throws Exception {
		when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);
	}

	@Test
	void shouldExportMementoCSV() throws Exception {
		doNothing().when(caretaker).serializeInterventionMementosAsCSV();
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/intervention-memento/export-csv")
				.accept("text/csv"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/csv"))
				.andReturn().getResponse();
		
		assertEquals(response.getHeader("content-disposition"), "attachment; filename=\"interventionMemento.csv\"");
		assertEquals(response.getHeader("Cache-Control"), "no-cache, no-store, must-revalidate");
		assertEquals(response.getHeader("pragma"), "no-cache");
		assertEquals(response.getHeader("Expires"), "0");
	}

	@Test
	void shouldReturnErrorWhenCSVCreationFails() throws Exception {
		doThrow(Exception.class).when(caretaker).serializeInterventionMementosAsCSV();
		
		mockMvc.perform(get("/api/intervention-memento/export-csv")
				.accept("text/csv"))
				.andExpect(status().isInternalServerError());
	}

	@Test
	void shouldExportMementoCSVByDates() throws Exception {
		doNothing().when(caretaker).serializeInterventionMementosAsCSVByDates(any(LocalDate.class), any(LocalDate.class));
		
		MockHttpServletResponse response = mockMvc.perform(get("/api/intervention-memento/export-csv-dates?dateStart=2021-08-10&dateEnd=2021-08-12")
				.accept("text/csv"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/csv"))
				.andReturn().getResponse();
		
		assertEquals(response.getHeader("content-disposition"), "attachment; filename=\"interventionMementoDates.csv\"");
		assertEquals(response.getHeader("Cache-Control"), "no-cache, no-store, must-revalidate");
		assertEquals(response.getHeader("pragma"), "no-cache");
		assertEquals(response.getHeader("Expires"), "0");
	}
	
	@Test
	void shouldReturnErrorWhenCSVCreationByDatesFails() throws Exception {
		doThrow(Exception.class).when(caretaker).serializeInterventionMementosAsCSVByDates(any(LocalDate.class), any(LocalDate.class));
		
		mockMvc.perform(get("/api/intervention-memento/export-csv-dates?dateStart=2021-08-10&dateEnd=2021-08-12")
				.accept("text/csv"))
				.andExpect(status().isInternalServerError());
	}

}
