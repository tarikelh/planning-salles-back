package fr.dawan.calendarproject.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.ResourceDto;
import fr.dawan.calendarproject.interceptors.TokenInterceptor;
import fr.dawan.calendarproject.services.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
class ResourceControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ResourceController resourceController;

	@MockBean
	private ResourceService resourceService;

	@MockBean
	private TokenInterceptor tokenInterceptor;

	private List<ResourceDto> resources = new ArrayList<ResourceDto>();

	@BeforeEach()
	public void beforeEach() throws Exception {
		when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);

		resources.add(new ResourceDto(1, 0, 10, "Casque", 1 ));
		resources.add(new ResourceDto(2, 0, 10, "Ecran", 1 ));
		resources.add(new ResourceDto(3, 0, 10, "Table", 1 ));
	}

	@Test
	void contextLoads() {
		assertThat(resourceController).isNotNull();
	}

	@Test
	void shouldFetchAllResources() throws Exception {
		when(resourceService.getAllResources()).thenReturn(resources);

		mockMvc.perform(get("/api/resources").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(resources.size()))).andExpect(jsonPath("$[0].name", is("Casque")));
	}

	@Test
	void shouldFetchAllResourcesPagination() throws Exception {
		when(resourceService.getAllResources(any(int.class), any(int.class), any(String.class))).thenReturn(resources);

		mockMvc.perform(get("/api/resources/pagination?page=0&size=0&search=").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(resources.size())))
				.andExpect(jsonPath("$[0].name", is("Casque")));
	}

	@Test
	void shouldFetchResourceById() throws Exception {
		final long resourceId = 2;
		when(resourceService.getById(resourceId)).thenReturn(resources.get(1));

		mockMvc.perform(get("/api/resources/{id}", resourceId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name", is("Ecran")));
	}

	@Test
	void shouldReturn404WhenGetById() throws Exception {
		final long resourceId = 10;
		when(resourceService.getById(resourceId)).thenReturn(null);

		mockMvc.perform(get("/api/resources/{id}", resourceId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldDeleteResourceById() throws Exception {
		final long resourceId = 1;
		doNothing().when(resourceService).deleteById(resourceId);

		String res = mockMvc.perform(delete("/api/resources/{id}", resourceId)).andExpect(status().isAccepted()).andReturn()
				.getResponse().getContentAsString();

		assertEquals("Resource with id " + resourceId + " deleted", res);
	}

	@Test
	void shouldReturn404WhenDeleteById() throws Exception {
		final long resourceId = 10;
		doThrow(IllegalArgumentException.class).when(resourceService).deleteById(resourceId);

		String res = mockMvc.perform(delete("/api/resources/{id}", resourceId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

		assertEquals(res, "Resource with id " + resourceId + " Not Found");
	}

	@Test
	void shouldCreateNewResource() throws Exception {

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

		ResourceDto newResource = new ResourceDto(0, 0, 10, "Chaise", 1);
		ResourceDto resMock = new ResourceDto(4, 0, 10, "Chaise", 1);
		String newResourceJson = objectMapper.writeValueAsString(newResource);

		when(resourceService.saveOrUpdate(any(ResourceDto.class))).thenReturn(resMock);

		mockMvc.perform(post("/api/resources").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(newResourceJson)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(4))).andExpect(jsonPath("$.name", is(newResource.getName())))
				.andExpect(jsonPath("$.version", is(newResource.getVersion())));
	}

	@Test
	void shouldUpdateResource() throws Exception {

		ResourceDto updated = new ResourceDto(resources.get(0).getId(), resources.get(0).getVersion(), resources.get(0).getQuantity(),
				resources.get(0).getName(), resources.get(0).getRoomId());

		updated.setName("Casque Corsair");

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String updatedJson = objectMapper.writeValueAsString(updated);

		when(resourceService.saveOrUpdate(any(ResourceDto.class))).thenReturn(updated);

		mockMvc.perform(put("/api/resources").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(updatedJson)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is(updated.getName())))
				.andExpect(jsonPath("$.name", not(resources.get(0).getName())));
	}

	@Test
	void shouldReturn404WhenUpdateWithWrongId() throws Exception {

		ResourceDto newResource = new ResourceDto(120, 0, 10, "Ciseaux", 1);

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String newResourceJson = objectMapper.writeValueAsString(newResource);

		when(resourceService.saveOrUpdate(newResource)).thenReturn(null);

		String res = mockMvc.perform(put("/api/resources").contentType(MediaType.APPLICATION_JSON).content(newResourceJson))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

		assertEquals(res, "Resource with id " + newResource.getId() + " Not Found");
	}

	@Test
	void shouldFetchResourceByAvailability() throws Exception {
		final boolean availability = true;
		when(resourceService.getResourceByRoomAvailability(any(Boolean.class))).thenReturn(resources);

		mockMvc.perform(get("/api/resources/availability", availability).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].name", is("Casque")));
	}

	@Test
	void shouldFetchResourceByQuantity() throws Exception {
		final int quantity = 10;
		when(resourceService.getResourceByQuantity(quantity)).thenReturn(resources);

		mockMvc.perform(get("/api/resources/quantity",quantity).param("quantity","10").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].name", is("Casque")));
	}

	@Test
	void shouldFetchResourceByQuantityRange() throws Exception {
		final int value1 = 9;
		final int value2 = 11;
		when(resourceService.getResourceByQuantityRange(value1,value2)).thenReturn(resources);

		mockMvc.perform(get("/api/resources/quantityRange?value1=9&value2=11").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$[0].name",is(resources.get(0).getName())));
	}

	@Test
	void shouldFetchCountDto() throws Exception {
		CountDto countDto = new CountDto(3);

		when(resourceService.count(any(String.class))).thenReturn(countDto);

		mockMvc.perform(get("/api/resources/count").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nb", is(resources.size())));
	}

}
