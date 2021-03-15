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
import java.time.LocalDate;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.controllers.InterventionController;
import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.dto.EmployeeDto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.Employee;

@SpringBootTest
@AutoConfigureMockMvc
class CalendarprojectApplicationTestsIntervention {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private InterventionController interventionController;
	
	@Autowired
	private ObjectMapper objectMapper; // objet permettant de convertir String <=> JSON/XML
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	LocalDate localDate = LocalDate.now();
	
	@BeforeEach()
	public void beforeEach() throws Exception {
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); //peut le faire dabs ContactDto aussi @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")"
		objectMapper.setDateFormat(df);
	}

	
	@Test
	void contextLoads() {
		assertThat(interventionController).isNotNull();
	}
	
	
	@Test
	void testFindAll() {
		try {
			mockMvc.perform(get("/api/interventions").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
					.andExpect(jsonPath("$[0].dateStart", is("2021-03-10")))
					.andExpect(jsonPath("$[0].employee.name", is("Ahmed")))
					.andExpect(jsonPath("$[0].course.title", is("Java Advanced ++")));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	void testFindById() {
		try {
			byte[] arr = mockMvc
					.perform(get("/api/interventions/1").accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
					.andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray();
			String jsonRep = new String(arr, StandardCharsets.UTF_8);

			InterventionDto intDto = objectMapper.readValue(jsonRep, InterventionDto.class);

			//Pb avec la date		
			//intDto.getDateStart()
			assertEquals("2021-03-10", df.format(intDto.getDateStart()));
			assertEquals("Ahmed", intDto.getEmployee().getName());
			assertEquals("Java Advanced ++", intDto.getCourse().getTitle());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	void testSave() {
		try {
			
			InterventionDto intToInsert = new InterventionDto();
			intToInsert.setDateStart(localDate.parse("2020-03-15"));
			intToInsert.setDateEnd(localDate.parse("2020-03-20"));
			EmployeeDto emp1 = new EmployeeDto();
			emp1.setId(1);
			intToInsert.setEmployee(emp1);
			

			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			String jsonReq = objectMapper.writeValueAsString(intToInsert);

			byte[] jsonReponseBytes = mockMvc.perform(post("/api/courses") // VERB + URI
					.contentType(MediaType.APPLICATION_JSON) // consumes MIME TYPE
					.characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON) // produces MIME TYPE
					.content(jsonReq)).andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray();
			String jsonReponse = new String(jsonReponseBytes, StandardCharsets.UTF_8);

			InterventionDto intDto = objectMapper.readValue(jsonReponse, InterventionDto.class);
			assertTrue(intDto.getId() != 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
		
	}
	
	
	@Test
	void testUpdate() {
		/*
		try {

			InterventionDto courseToupdate = interventionController.getById(3);
			courseToupdate.setTitle("Angular");;

			String jsonReq = objectMapper.writeValueAsString(courseToupdate);

			String jsonReponse = mockMvc.perform(put("/api/courses") // VERB + URI
					.contentType(MediaType.APPLICATION_JSON) // consumes MIME TYPE
					.accept(MediaType.APPLICATION_JSON) // produces MIME TYPE
					.content(jsonReq)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

			InterventionDto cDto = objectMapper.readValue(jsonReponse, InterventionDto.class);
			assertEquals(3, cDto.getId());
			assertEquals("Angular", cDto.getTitle());

		} catch (Exception e) {
			fail(e.getMessage());
		}
		*/
	}
	
	
	@Test
	void testDelete() {
		/*
		try {
		
		}
			//InterventionDto cDto = new InterventionDto();
			//cDto.setId(5);

			byte[] jsonReponseBytes =  mockMvc.perform(delete("/api/courses/5").accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isAccepted()).andReturn().getResponse().getContentAsByteArray();
			String jsonReponse = new String(jsonReponseBytes, StandardCharsets.UTF_8);

			//assertNull(cDto.getEmail());
			
			assertEquals("suppression effectuée", jsonReponse);

		} catch (Exception e) {
			fail(e.getMessage());
		}
		*/
	}

}
