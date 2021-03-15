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

import fr.dawan.calendarproject.controllers.CourseController;
import fr.dawan.calendarproject.dto.CourseDto;

@SpringBootTest
@AutoConfigureMockMvc
class CalendarprojectApplicationTestsCourse {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CourseController courseController;
	
	@Autowired
	private ObjectMapper objectMapper; // objet permettant de convertir String <=> JSON/XML
	
	
	@BeforeEach()
	public void beforeEach() throws Exception {
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); //peut le faire dabs ContactDto aussi @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")"
		objectMapper.setDateFormat(df);
	}

	
	@Test
	void contextLoads() {
		assertThat(courseController).isNotNull();
	}
	
	
	@Test
	void testFindAll() {
		try {
			mockMvc.perform(get("/api/courses").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
					.andExpect(jsonPath("$[0].title", is("Java Advanced ++")));
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
					.perform(get("/api/courses/1").accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
					.andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray();
			String jsonRep = new String(arr, StandardCharsets.UTF_8);

			// transform le String en ContactDto
			CourseDto cDto = objectMapper.readValue(jsonRep, CourseDto.class);

			assertEquals("Java Advanced ++", cDto.getTitle());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	void testSave() {
		try {
			CourseDto courseToInsert = new CourseDto();
			courseToInsert.setTitle("Vue JS");

			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			String jsonReq = objectMapper.writeValueAsString(courseToInsert);

			byte[] jsonReponseBytes = mockMvc.perform(post("/api/courses") // VERB + URI
					.contentType(MediaType.APPLICATION_JSON) // consumes MIME TYPE
					.characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON) // produces MIME TYPE
					.content(jsonReq)).andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray();
			String jsonReponse = new String(jsonReponseBytes, StandardCharsets.UTF_8);

			CourseDto cDto = objectMapper.readValue(jsonReponse, CourseDto.class);
			assertTrue(cDto.getId() != 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	void testUpdate() {
		try {

			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

			CourseDto courseToupdate = courseController.getById(3);
			courseToupdate.setTitle("Angular");;

			String jsonReq = objectMapper.writeValueAsString(courseToupdate);

			String jsonReponse = mockMvc.perform(put("/api/courses") // VERB + URI
					.contentType(MediaType.APPLICATION_JSON) // consumes MIME TYPE
					.accept(MediaType.APPLICATION_JSON) // produces MIME TYPE
					.content(jsonReq)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

			CourseDto cDto = objectMapper.readValue(jsonReponse, CourseDto.class);
			assertEquals(3, cDto.getId());
			assertEquals("Angular", cDto.getTitle());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	void testDelete() {
		try {
			//CourseDto cDto = new CourseDto();
			//cDto.setId(5);

			byte[] jsonReponseBytes =  mockMvc.perform(delete("/api/courses/5").accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isAccepted()).andReturn().getResponse().getContentAsByteArray();
			String jsonReponse = new String(jsonReponseBytes, StandardCharsets.UTF_8);

			//assertNull(cDto.getEmail());
			
			assertEquals("suppression effectuée", jsonReponse);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
