package fr.dawan.calendarproject.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.RoomDto;
import fr.dawan.calendarproject.interceptors.TokenInterceptor;
import fr.dawan.calendarproject.services.RoomService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RoomController roomController;

	@MockBean
	private RoomService roomService;

	@MockBean
	private TokenInterceptor tokenInterceptor;

	private List<RoomDto> rooms = new ArrayList<RoomDto>();

	@BeforeEach()
	public void beforeEach() throws Exception {
		when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);

		rooms.add(new RoomDto(1, 1, "Room 1", 25 , true , 1 , 0  ));
		rooms.add(new RoomDto(2, 2, "Room 2", 25 , true , 1 , 0 ));
		rooms.add(new RoomDto(3, 3, "Room 3", 25 , true , 1 , 0 ));
	}

	@Test
	void contextLoads() {
		assertThat(roomController).isNotNull();
	}

	@Test
	void shouldFetchAllRooms() throws Exception {
		when(roomService.getAllRooms()).thenReturn(rooms);

		mockMvc.perform(get("/api/rooms").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(rooms.size()))).andExpect(jsonPath("$[0].name", is("Room 1")));
	}

	@Test
	void shouldFetchAllRoomsPagination() throws Exception {
		when(roomService.getAllRooms(any(int.class), any(int.class), any(String.class))).thenReturn(rooms);

		mockMvc.perform(get("/api/rooms/pagination?page=0&size=0&search=").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(rooms.size())))
				.andExpect(jsonPath("$[0].name", is("Room 1")));
	}

	@Test
	void shouldFetchRoomById() throws Exception {
		final long roomId = 2;
		when(roomService.getById(roomId)).thenReturn(rooms.get(1));

		mockMvc.perform(get("/api/rooms/{id}", roomId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name", is("Room 2")));
	}

	@Test
	void shouldReturn404WhenGetById() throws Exception {
		final long roomId = 10;
		when(roomService.getById(roomId)).thenReturn(null);

		mockMvc.perform(get("/api/rooms/{id}", roomId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldDeleteRoomById() throws Exception {
		final long roomId = 1;
		doNothing().when(roomService).deleteById(roomId);

		String res = mockMvc.perform(delete("/api/rooms/{id}", roomId)).andExpect(status().isAccepted()).andReturn()
				.getResponse().getContentAsString();

		assertEquals("Room with Id " + roomId + " Deleted", res);
	}

	@Test
	void shouldReturn404WhenDeleteById() throws Exception {
		final long roomId = 10;
		doThrow(IllegalArgumentException.class).when(roomService).deleteById(roomId);

		String res = mockMvc.perform(delete("/api/rooms/{id}", roomId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

		assertEquals(res, "Room with Id " + roomId + " Not Found");
	}

	@Test
	void shouldCreateNewRoom() throws Exception {

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

		RoomDto newRoom = new RoomDto(0, 0, "Room 4", 25 , true , 1 , 0);
		RoomDto resMock = new RoomDto(4, 0, "Room 4", 25 , true , 1 , 0);
		String newRoomJson = objectMapper.writeValueAsString(newRoom);

		when(roomService.saveOrUpdate(any(RoomDto.class))).thenReturn(resMock);

		mockMvc.perform(post("/api/rooms").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(newRoomJson)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(4))).andExpect(jsonPath("$.name", is(newRoom.getName())))
				.andExpect(jsonPath("$.version", is(newRoom.getVersion())));
	}

	@Test
	void shouldUpdateRoom() throws Exception {

		RoomDto updated = new RoomDto(rooms.get(0).getId(), rooms.get(0).getIdDg2(), rooms.get(0).getName(),
				rooms.get(0).getFullCapacity(), rooms.get(0).getIsAvailable(), rooms.get(0).getLocationId(), rooms.get(0).getVersion());

		updated.setName("Room 4.5");

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String updatedJson = objectMapper.writeValueAsString(updated);

		when(roomService.saveOrUpdate(any(RoomDto.class))).thenReturn(updated);

		mockMvc.perform(put("/api/rooms").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON).content(updatedJson)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is(updated.getName())))
				.andExpect(jsonPath("$.name", not(rooms.get(0).getName())));
	}

	@Test
	void shouldReturn404WhenUpdateWithWrongId() throws Exception {

		RoomDto newRoom = new RoomDto(120, 0, "Room 4", 25 , true , 1 , 0);

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String newRoomJson = objectMapper.writeValueAsString(newRoom);

		when(roomService.saveOrUpdate(newRoom)).thenReturn(null);

		String res = mockMvc.perform(put("/api/rooms").contentType(MediaType.APPLICATION_JSON).content(newRoomJson))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

		assertEquals(res, "Room with Id " + newRoom.getId() + " Not Found");
	}


	@Test
	void shouldFetchCountDto() throws Exception {
		CountDto countDto = new CountDto(3);

		when(roomService.count(any(String.class))).thenReturn(countDto);

		mockMvc.perform(get("/api/rooms/count").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nb", is(rooms.size())));
	}

}
