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
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.controllers.UserController;
import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.enums.UserType;

@SpringBootTest
@AutoConfigureMockMvc
class CalendarprojectApplicationTestsUser {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserController employeeController;

	@Autowired
	private ObjectMapper objectMapper; // objet permettant de convertir String <=> JSON/XML

	@BeforeEach()
	public void beforeEach() throws Exception {
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // peut le faire dabs ContactDto aussi
																	// @JsonFormat(shape = Shape.STRING, pattern =
																	// "yyyy-MM-dd")"
		objectMapper.setDateFormat(df);
	}

	@Test
	void contextLoads() {
		assertThat(employeeController).isNotNull();
	}

	@Test
	void testFindAll() {
		try {
			mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
					.andExpect(jsonPath("$[0].name", is("Admin")));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	void testFindById() {
		try {

			// Exemple 2 : on récupère la réponse json et on la parse comme on le souhaite
			// characterEncoding("utf-8")) s'il y a des accents dans la BDD
			byte[] arr = mockMvc
					.perform(get("/api/users/1").accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
					.andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray();
			String jsonRep = new String(arr, StandardCharsets.UTF_8);

			// transform le String en ContactDto
			AdvancedUserDto empDto = objectMapper.readValue(jsonRep, AdvancedUserDto.class);

			assertEquals("Admin", empDto.getFirstName());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	void testSave() {
		try {
			AdvancedUserDto empToInsert = new AdvancedUserDto();
			empToInsert.setFirstName("Jade");
			empToInsert.setType(UserType.COMMERCIAL);

			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			String jsonReq = objectMapper.writeValueAsString(empToInsert);

			byte[] jsonReponseBytes = mockMvc.perform(post("/api/users") // VERB + URI
					.contentType(MediaType.APPLICATION_JSON) // consumes MIME TYPE
					.characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON) // produces MIME TYPE
					.content(jsonReq)).andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray();
			String jsonReponse = new String(jsonReponseBytes, StandardCharsets.UTF_8);

			AdvancedUserDto empDto = objectMapper.readValue(jsonReponse, AdvancedUserDto.class);
			assertTrue(empDto.getId() != 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	void testUpdate() {
		try {
			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

			AdvancedUserDto empToupdate = employeeController.getById(2);
			empToupdate.setFirstName("AhmedModif");
			empToupdate.setType(UserType.ADMINISTRATIF);

			String jsonReq = objectMapper.writeValueAsString(empToupdate);

			String jsonReponse = mockMvc.perform(put("/api/users") // VERB + URI
					.contentType(MediaType.APPLICATION_JSON) // consumes MIME TYPE
					.accept(MediaType.APPLICATION_JSON) // produces MIME TYPE
					.content(jsonReq)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

			AdvancedUserDto empDto = objectMapper.readValue(jsonReponse, AdvancedUserDto.class);
			assertEquals(2, empDto.getId());
			assertEquals("AhmedModif", empDto.getFirstName());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	void testDelete() {
		try {
			// EmployeeDto empDto = new EmployeeDto();
			// empDto.setId(4);

			byte[] jsonReponseBytes = mockMvc.perform(delete("/api/users/4").accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isAccepted()).andReturn().getResponse().getContentAsByteArray();
			String jsonReponse = new String(jsonReponseBytes, StandardCharsets.UTF_8);

			// assertNull(empDto.getName());

			assertEquals("suppression effectuée", jsonReponse);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
