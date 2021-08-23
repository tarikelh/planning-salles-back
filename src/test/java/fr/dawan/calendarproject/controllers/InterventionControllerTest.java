package fr.dawan.calendarproject.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.services.InterventionService;

@SpringBootTest
@AutoConfigureMockMvc
class InterventionControllerTest {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private InterventionController interventionController;
	
	@MockBean
	private InterventionService interventionService;
	
	private List<InterventionDto> intervs =  new ArrayList<InterventionDto>();
	
	@BeforeEach()
	public void beforeEach() throws Exception {
		intervs.add(new InterventionDto());
	}
	
	@Test
	void contextLoads() {
		assertThat(interventionController).isNotNull();
	}
	
	@Test
	void shouldFetchInterventions() {
		
	}

	@Test
	void testGetById() {
		fail("Not yet implemented");
	}

	@Test
	void testGetMasterIntervention() {
		fail("Not yet implemented");
	}

	@Test
	void testGetSubInterventions() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllMementoCSV() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllMementoCSVDates() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteById() {
		fail("Not yet implemented");
	}

	@Test
	void testSave() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	void testSearchByCourse() {
		fail("Not yet implemented");
	}

	@Test
	void testGetFromUserByDateRange() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllByDateRange() {
		fail("Not yet implemented");
	}

	@Test
	void testExportUserInteventions() {
		fail("Not yet implemented");
	}

	@Test
	void testCountByUserTypeNoMaster() {
		fail("Not yet implemented");
	}

}
