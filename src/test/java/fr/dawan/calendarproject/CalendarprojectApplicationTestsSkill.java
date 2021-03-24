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

import fr.dawan.calendarproject.controllers.SkillController;
import fr.dawan.calendarproject.dto.AvancedSkillDto;

@SpringBootTest
@AutoConfigureMockMvc
class CalendarprojectApplicationTestsSkill {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SkillController skillController;
	
	@Autowired
	private ObjectMapper objectMapper; // objet permettant de convertir String <=> JSON/XML
	
	
	@BeforeEach()
	public void beforeEach() throws Exception {
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
	}

	
	@Test
	void contextLoads() {
		assertThat(skillController).isNotNull();
	}
	
	
	@Test
	void testFindAll() {
		try {
			mockMvc.perform(get("/api/skills").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
					.andExpect(jsonPath("$[0].title", is("Angular")));
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
					.perform(get("/api/skills/1").accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
					.andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray();
			String jsonRep = new String(arr, StandardCharsets.UTF_8);

			// transform le String en ContactDto
			AvancedSkillDto sDto = objectMapper.readValue(jsonRep, AvancedSkillDto.class);

			assertEquals("Angular", sDto.getTitle());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	void testSave() {
		try {
			AvancedSkillDto skillToInsert = new AvancedSkillDto();
			skillToInsert.setTitle("C#");

			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			String jsonReq = objectMapper.writeValueAsString(skillToInsert);

			byte[] jsonReponseBytes = mockMvc.perform(post("/api/skills") // VERB + URI
					.contentType(MediaType.APPLICATION_JSON) // consumes MIME TYPE
					.characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON) // produces MIME TYPE
					.content(jsonReq)).andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray();
			String jsonReponse = new String(jsonReponseBytes, StandardCharsets.UTF_8);

			AvancedSkillDto sDto = objectMapper.readValue(jsonReponse, AvancedSkillDto.class);
			assertTrue(sDto.getId() != 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	void testUpdate() {
		try {

			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

			AvancedSkillDto skillToupdate = skillController.getById(3);
			skillToupdate.setTitle("C# et .NET");;

			String jsonReq = objectMapper.writeValueAsString(skillToupdate);

			String jsonReponse = mockMvc.perform(put("/api/skills") // VERB + URI
					.contentType(MediaType.APPLICATION_JSON) // consumes MIME TYPE
					.accept(MediaType.APPLICATION_JSON) // produces MIME TYPE
					.content(jsonReq)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

			AvancedSkillDto sDto = objectMapper.readValue(jsonReponse, AvancedSkillDto.class);
			assertEquals(3, sDto.getId());
			assertEquals("C# et .NET", sDto.getTitle());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	void testDelete() {
		try {

			byte[] jsonReponseBytes =  mockMvc.perform(delete("/api/skills/3").accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isAccepted()).andReturn().getResponse().getContentAsByteArray();
			String jsonReponse = new String(jsonReponseBytes, StandardCharsets.UTF_8);
			
			assertEquals("suppression effectuée", jsonReponse);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
