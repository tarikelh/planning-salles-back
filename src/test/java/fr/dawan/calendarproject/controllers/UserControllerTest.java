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
	void shouldFetchAllUsersPagination() throws Exception {
		when(userService.getAllUsers(-1, -1, "")).thenReturn(users);
		
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
	void shouldFetchUserById() throws Exception {
		final long userId = 3;
		when(userService.getById(userId)).thenReturn(users.get(2));
		
		mockMvc.perform(get("/api/users/{id}", userId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(3)))
				.andExpect(jsonPath("$.firstName", is(users.get(2).getFirstName())))
				.andExpect(jsonPath("$.lastName", is(users.get(2).getLastName())));
	}
	
	@Test
	void shouldReturn404WhenGetById() throws Exception {
		final long userId = 5;
		when(userService.getById(userId)).thenReturn(null);
		
		String res = mockMvc.perform(get("/api/users/{id}", userId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();

		assertEquals(res, "User with id " + userId + " Not Found");
	}

	@Test
	void shouldDeleteById() throws Exception {
		final long userId = 1;
		doNothing().when(userService).deleteById(userId);

		String res = mockMvc.perform(delete("/api/users/{id}", userId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andReturn().getResponse().getContentAsString();

		assertEquals("User with id " + userId + " Deleted", res);
	}

	@Test
	void shouldReturn404WhenDeleteById() throws Exception {
		final long userId = 42;
		doThrow(IllegalArgumentException.class).when(userService).deleteById(userId);

		String res = mockMvc.perform(delete("/api/users/{id}", userId)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();

		assertEquals(res, "User with id " + userId + " Not Found");
	}
	
	@Test
	void shouldCreateNewUser() throws Exception {
		AdvancedUserDto newUser = new AdvancedUserDto(0, "Françis", "Cabrel", 1,
				"fcabrel@dawan.fr", "testPasswordCabrel",
				"FORMATEUR", "JEHANN", "", 0, null);
		AdvancedUserDto resUser = new AdvancedUserDto(4, "Françis", "Cabrel", 1,
				"fcabrel@dawan.fr", "testPasswordCabrel",
				"FORMATEUR", "JEHANN", "", 0, null);
		
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String newUserJson = objectMapper.writeValueAsString(newUser);
		
		when(userService.saveOrUpdate(any(AdvancedUserDto.class))).thenReturn(resUser);
		
		mockMvc.perform(post("/api/users")
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(newUserJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(4)))
				.andExpect(jsonPath("$.firstName", is(newUser.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(newUser.getLastName())));
	}

	@Test
	void shouldUpdateUser() throws Exception {
		AdvancedUserDto updatedUser = users.get(0);
		
		final String oldFirstName = updatedUser.getFirstName();
		final String oldLastName = updatedUser.getLastName();
		final String oldEmail = updatedUser.getEmail();
		
		updatedUser.setFirstName("Alain");
		updatedUser.setLastName("Souchon");
		updatedUser.setEmail("asouchon@dawan.fr");

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String updatedLocJson = objectMapper.writeValueAsString(updatedUser);
		
		when(userService.saveOrUpdate(any(AdvancedUserDto.class))).thenReturn(updatedUser);
		
		mockMvc.perform(put("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)
				.content(updatedLocJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.firstName", not(oldFirstName)))
				.andExpect(jsonPath("$.lastName", not(oldLastName)))
				.andExpect(jsonPath("$.email", not(oldEmail)))
				.andExpect(jsonPath("$.firstName", is(updatedUser.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(updatedUser.getLastName())))
				.andExpect(jsonPath("$.email", is(updatedUser.getEmail())));
	}

	@Test
	public void shouldReturn404WhenUpdateWithWrongId() throws Exception {
		
		AdvancedUserDto wrongIdUser = new AdvancedUserDto(150, "Françis", "Cabrel", 1,
				"fcabrel@dawan.fr", "testPasswordCabrel",
				"FORMATEUR", "JEHANN", "", 0, null);
		
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String wrongIdLocJson = objectMapper.writeValueAsString(wrongIdUser);
		
		when(userService.saveOrUpdate(wrongIdUser)).thenReturn(null);
		
		String res = mockMvc.perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(wrongIdLocJson))
				.andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		
		assertEquals(res, "User with id " + wrongIdUser.getId() + " Not Found");
	}
}
