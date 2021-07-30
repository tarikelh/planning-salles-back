package fr.dawan.calendarproject.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.fail;
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

import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
	
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	UserController userController;
	
	@MockBean
	UserService userService;
	
	private List<AdvancedUserDto> users = new ArrayList<AdvancedUserDto>();
	
	@BeforeEach
	public void beforeEach() throws Exception {
		users.add(new AdvancedUserDto(1, "Daniel", "Balavoine", 1,
				"dbalavoine@dawan.fr", "testPassword",
				"ADMINISTRATIF", "DAWAN", "", 0, null));
		
		users.add(new AdvancedUserDto(2, "Michel", "Polnareff", 1,
				"mpolnareff@dawan.fr", "testPasswordPolnareff",
				"COMMERCIAL", "JEHANN", "", 0, null));
		
		users.add(new AdvancedUserDto(3, "Charles", "Aznavour", 1,
				"caznavour@dawan.fr", "testPasswordAznav",
				"FORMATEUR", "JEHANN", "", 0, null));
	}
	
	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
	}
	
	@Test
	void shouldFetchAllUsers() throws Exception {
		when(userService.getAllUsers()).thenReturn(users);
		
		mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(users.size())))
				.andExpect(jsonPath("$[2].lastName", is(users.get(2).getLastName())));
	}

	@Test
	void shouldFetchAllUsersByType() throws Exception {
		String userType = "ADMINISTRATIF";
		List<AdvancedUserDto> filteredUsers = new ArrayList<AdvancedUserDto>();
		filteredUsers.add(users.get(0));
		
		when(userService.getAllUsersByType(userType)).thenReturn(filteredUsers);
		
		mockMvc.perform(get("/api/users/search/{type}", userType).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(filteredUsers.size())))
				.andExpect(jsonPath("$[0].lastName", is(filteredUsers.get(0).getLastName())));
	}

	@Test
	void testGetById() {
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

}
