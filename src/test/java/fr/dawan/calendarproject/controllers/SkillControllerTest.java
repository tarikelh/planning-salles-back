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

import fr.dawan.calendarproject.dto.AdvancedSkillDto;
import fr.dawan.calendarproject.interceptors.TokenInterceptor;
import fr.dawan.calendarproject.services.SkillService;

@SpringBootTest
@AutoConfigureMockMvc
public class SkillControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	SkillController skillController;
	
	@MockBean
	SkillService skillService;

	@MockBean
	private TokenInterceptor tokenInterceptor;

	private List<AdvancedSkillDto> skills = new ArrayList<AdvancedSkillDto>();
	
	@BeforeEach()
	public void beforeEach() throws Exception {
		when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);

		skills.add(new AdvancedSkillDto(1, "DevOps", 0, null));
		skills.add(new AdvancedSkillDto(2, "POO", 0, null));
		skills.add(new AdvancedSkillDto(3, "SQL", 0, null));
	}
	
	@Test
	void contextLoads() {
		assertThat(skillController).isNotNull();
	}
	
	@Test
	public void shouldFetchAllSkills() throws Exception {
		when(skillService.getAllSkills()).thenReturn(skills);
		
		mockMvc.perform(get("/api/skills").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(skills.size())))
				.andExpect(jsonPath("$[2].title", is(skills.get(2).getTitle())));
	}
	
	@Test
	public void shouldFetchAllSkillsPagination() throws Exception {
		when(skillService.getAllSkills(-1, -1, "")).thenReturn(skills);
		
		mockMvc.perform(get("/api/skills").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(skills.size())))
				.andExpect(jsonPath("$[2].title", is(skills.get(2).getTitle())));
	}

	@Test
	public void shouldFetchOneSkillById() throws Exception {
		long skillId = 2;
		when(skillService.getById(skillId)).thenReturn(skills.get(1));
		
		mockMvc.perform(get("/api/skills/{id}", skillId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(2)))
				.andExpect(jsonPath("$.title", is(skills.get(1).getTitle())));
	}
	
	@Test
	public void shouldReturn404WhenWrongId() throws Exception {
		final long skillId = 12;
		when(skillService.getById(skillId)).thenReturn(null);
		
		mockMvc.perform(get("/api/skills/{id}", skillId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void shouldDeleteSkillById() throws Exception {
		final long skillId = 1;
		doNothing().when(skillService).deleteById(skillId);
		
		String res = mockMvc.perform(delete("/api/skills/{id}", skillId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andReturn().getResponse().getContentAsString();
		
		assertEquals(res,"Skill with Id " + skillId + " Deleted");
	}
	
	@Test
	public void shouldReturn404WhenDeleteWithWrongId() throws Exception {
		final long skillId = 10;
		doThrow(IllegalArgumentException.class).when(skillService).deleteById(skillId);
		
		String res = mockMvc.perform(delete("/api/skills/{id}", skillId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		
		assertEquals(res,"Skill with Id " + skillId + " Not Found");
	}

	@Test
	public void shouldCreateNewSkill() throws Exception {
		AdvancedSkillDto skillToCreate = new AdvancedSkillDto(0, "DevOps", 0, null);
		
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String skillToCreateJson = objectMapper.writeValueAsString(skillToCreate);
		
		when(skillService.saveOrUpdate(any(AdvancedSkillDto.class))).thenReturn(skills.get(0));
		
		mockMvc.perform(post("/api/skills").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(skillToCreateJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", not(skillToCreate.getId())))
				.andExpect(jsonPath("$.title", is(skillToCreate.getTitle())));
	}

	@Test
	public void shouldUpdateSkill() throws Exception {
		AdvancedSkillDto skillToUpdate = new AdvancedSkillDto(1, "SuperDevOps", 0, null);
		
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String skillToUpdateJson = objectMapper.writeValueAsString(skillToUpdate);
		
		when(skillService.saveOrUpdate(any(AdvancedSkillDto.class))).thenReturn(skillToUpdate);
		
		mockMvc.perform(put("/api/skills/")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)
				.content(skillToUpdateJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.title", is(skillToUpdate.getTitle())));
	}
	
	@Test
	public void shouldReturn404WhenUpdateWrongId() throws Exception {
		AdvancedSkillDto skillToUpdate = new AdvancedSkillDto(1, "SuperDevOps", 0, null);
		
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String skillToUpdateJson = objectMapper.writeValueAsString(skillToUpdate);
		
		when(skillService.saveOrUpdate(skillToUpdate)).thenReturn(skillToUpdate);
		
		String res = mockMvc.perform(put("/api/skills/")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)
				.content(skillToUpdateJson))
				.andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		
		assertEquals(res, "Skill with Id " + skillToUpdate.getId() + " Not Found");
	}

}
