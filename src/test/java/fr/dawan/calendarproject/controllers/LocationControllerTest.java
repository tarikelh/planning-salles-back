package fr.dawan.calendarproject.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.interceptors.TokenInterceptor;
import fr.dawan.calendarproject.services.LocationService;

@SpringBootTest
@AutoConfigureMockMvc
class LocationControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	LocationController locationController;

	@MockBean
	LocationService locationService;
	@MockBean
	private TokenInterceptor tokenInterceptor;

	private List<LocationDto> locs = new ArrayList<LocationDto>();

	@BeforeEach()
	public void beforeEach() throws Exception {
		when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);

		locs.add(new LocationDto(1, 1, "Paris", "#40A497", "FR", false, 0));
		locs.add(new LocationDto(2, 2, "Nantes", "#9859C6", "FR", false, 0));
		locs.add(new LocationDto(3, 3, "Nogent-Le-Rotrou", "#59C674", "FR", false, 0));
	}

	@Test
	void contextLoads() {
		assertThat(locationController).isNotNull();
	}

	@Test
	void shouldFetchAllLocations() throws Exception {

		when(locationService.getAllLocations()).thenReturn(locs);

		mockMvc.perform(get("/api/locations").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(locs.size())))
				.andExpect(jsonPath("$[0].city", is(locs.get(0).getCity())))
				.andExpect(jsonPath("$[0].color", is(locs.get(0).getColor())));
	}

	@Test
	void shouldFetchAllLocationsPagination() throws Exception {

		when(locationService.getAllLocations(any(int.class), any(int.class), any(String.class))).thenReturn(locs);

		mockMvc.perform(get("/api/locations/pagination?page=0&size=3&search=test").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(locs.size())))
				.andExpect(jsonPath("$[0].city", is(locs.get(0).getCity())))
				.andExpect(jsonPath("$[0].color", is(locs.get(0).getColor())));
	}

	@Test
	void shouldFetchLocationById() throws Exception {
		final long locId = 2;
		when(locationService.getById(locId)).thenReturn(locs.get(1));

		mockMvc.perform(get("/api/locations/{id}", locId).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.city", is(locs.get(1).getCity())))
				.andExpect(jsonPath("$.color", is(locs.get(1).getColor())));
	}

	@Test
	void shouldReturn404WhenGetById() throws Exception {
		final long locId = 15;
		when(locationService.getById(locId)).thenReturn(null);

		mockMvc.perform(get("/api/locations/{id}", locId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void ShouldDeleteLocationById() throws Exception {
		final long locId = 1;
		doNothing().when(locationService).deleteById(locId);

		String res = mockMvc.perform(delete("/api/locations/{id}", locId)).andExpect(status().isAccepted()).andReturn()
				.getResponse().getContentAsString();

		assertEquals("Location with id " + locId + " Deleted", res);
	}

	@Test
	void shouldReturn404WhenDeleteById() throws Exception {
		final long locId = 42;
		doThrow(IllegalArgumentException.class).when(locationService).deleteById(locId);

		String res = mockMvc.perform(delete("/api/locations/{id}", locId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

		assertEquals(res, "Location with id " + locId + " Not Found");
	}

	@Test
	void ShouldCreateNewLocation() throws Exception {

		LocationDto newLoc = new LocationDto(0, 0, "Lyon", "#C33E42", "FR", false, 0);
		LocationDto resMock = new LocationDto(4, 4, "Lyon", "#C33E42", "FR", false, 0);

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String newLocJson = objectMapper.writeValueAsString(newLoc);

		when(locationService.saveOrUpdate(any(LocationDto.class))).thenReturn(resMock);

		mockMvc.perform(post("/api/locations").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(newLocJson)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(4))).andExpect(jsonPath("$.city", is(newLoc.getCity())))
				.andExpect(jsonPath("$.color", is(newLoc.getColor())));
	}

	@Test
	void shouldUpdateLocation() throws Exception {
		LocationDto updatedLoc = new LocationDto(locs.get(0).getId(), locs.get(0).getIdDg2(), locs.get(0).getCity(), locs.get(0).getColor(),
				locs.get(0).getCountryCode(),
				false, locs.get(0).getVersion());

		updatedLoc.setColor("#AED35B");

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String updatedLocJson = objectMapper.writeValueAsString(updatedLoc);

		when(locationService.saveOrUpdate(any(LocationDto.class))).thenReturn(updatedLoc);

		mockMvc.perform(put("/api/locations").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(updatedLocJson)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.city", is(locs.get(0).getCity())))
				.andExpect(jsonPath("$.color", not(locs.get(0).getColor())))
				.andExpect(jsonPath("$.color", is("#AED35B")));
	}

	@Test
	void shouldReturn404WhenUpdateWithWrongId() throws Exception {

		LocationDto wrongIdLoc = new LocationDto(12, 12, "Nantes", "#9859C6", "FR", false, 0);

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String wrongIdLocJson = objectMapper.writeValueAsString(wrongIdLoc);

		when(locationService.saveOrUpdate(wrongIdLoc)).thenReturn(null);

		String res = mockMvc
				.perform(put("/api/locations").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
						.accept(MediaType.APPLICATION_JSON).content(wrongIdLocJson))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

		assertEquals(res, "Location with id " + wrongIdLoc.getId() + " Not Found");
	}

}
