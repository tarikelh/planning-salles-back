package fr.dawan.calendarproject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.controllers.LocationController;
import fr.dawan.calendarproject.dto.LocationDto;

@SpringBootTest
@AutoConfigureMockMvc
class CalendarprojectApplicationTestsLocation {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private LocationController locationController;
	
	@Autowired
	private ObjectMapper objectMapper; // objet permettant de convertir String <=> JSON/XML
	
	
	@BeforeEach()
	public void beforeEach() throws Exception {
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
	}

	
	@Test
	void contextLoads() {
		assertThat(locationController).isNotNull();
	}
	
	
	@Test
	void testFindAll() {
		try {
			mockMvc.perform(get("/api/locations").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
					.andExpect(jsonPath("$[0].city", is("Paris")));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	void testFindById() {
		try {

			// Exemple 2 : on récupère la réponse json et on la parse comme on le souhaite
			//characterEncoding("utf-8")) s'il y a des accents dans la BDD
			byte[] arr = mockMvc
					.perform(get("/api/locations/1").accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
					.andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray();
			String jsonRep = new String(arr, StandardCharsets.UTF_8);

			// transform le String en ContactDto
			LocationDto cDto = objectMapper.readValue(jsonRep, LocationDto.class);

			assertEquals("Paris", cDto.getCity());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	void testSave() {
		try {
			LocationDto locationToInsert = new LocationDto();
			locationToInsert.setCity("NYC");
			locationToInsert.setColor("#7fff00");

			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			String jsonReq = objectMapper.writeValueAsString(locationToInsert);

			byte[] jsonReponseBytes = mockMvc.perform(post("/api/locationss") // VERB + URI
					.contentType(MediaType.APPLICATION_JSON) // consumes MIME TYPE
					.characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON) // produces MIME TYPE
					.content(jsonReq)).andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray();
			String jsonReponse = new String(jsonReponseBytes, StandardCharsets.UTF_8);

			LocationDto lDto = objectMapper.readValue(jsonReponse, LocationDto.class);
			assertTrue(lDto.getId() != 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	void testUpdate() {
		try {

			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

			LocationDto locationToupdate = locationController.getById(2);
			locationToupdate.setCity("Nantes 2");
			locationToupdate.setColor("#6495ed");

			String jsonReq = objectMapper.writeValueAsString(locationToupdate);

			String jsonReponse = mockMvc.perform(put("/api/locations") // VERB + URI
					.contentType(MediaType.APPLICATION_JSON) // consumes MIME TYPE
					.accept(MediaType.APPLICATION_JSON) // produces MIME TYPE
					.content(jsonReq)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

			LocationDto lDto = objectMapper.readValue(jsonReponse, LocationDto.class);
			assertEquals(2, lDto.getId());
			assertEquals("Nantes 2", lDto.getCity());
			assertEquals("#6495ed", lDto.getColor());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	void testDelete() {
		try {
			byte[] jsonReponseBytes =  mockMvc.perform(delete("/api/locations/3").accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isAccepted()).andReturn().getResponse().getContentAsByteArray();
			String jsonReponse = new String(jsonReponseBytes, StandardCharsets.UTF_8);

			
			assertEquals("suppression effectuée", jsonReponse);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
