package fr.dawan.calendarproject.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

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

import fr.dawan.calendarproject.dto.BookingDto;
import fr.dawan.calendarproject.interceptors.TokenInterceptor;
import fr.dawan.calendarproject.services.BookingService;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {

	@Autowired
	private BookingController bookingController;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookingService bookingService;

	@MockBean
	private TokenInterceptor tokenInterceptor;

	private List<BookingDto> bookings = new ArrayList<>();

	@BeforeEach
	public void beforeEach() throws Exception {

		when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);

		bookings.add(new BookingDto(1, LocalDate.parse("2022-04-01"), LocalDate.parse("2022-04-10"), 1, 1, 0));
		bookings.add(new BookingDto(2, LocalDate.parse("2022-05-01"), LocalDate.parse("2022-05-10"), 2, 2, 0));
		bookings.add(new BookingDto(3, LocalDate.parse("2022-06-01"), LocalDate.parse("2022-06-10"), 1, 3, 0));
		bookings.add(new BookingDto(4, LocalDate.parse("2022-07-01"), LocalDate.parse("2022-07-10"), 2, 1, 0));
		bookings.add(new BookingDto(5, LocalDate.parse("2022-08-01"), LocalDate.parse("2022-08-10"), 1, 2, 0));

	}

	@Test
	void contextLoads() {
		assertThat(bookingController).isNotNull();
	}

	@Test
	void shouldFetchAllBookings() throws Exception {

		when(bookingService.getAllBookings()).thenReturn(bookings);

		mockMvc.perform(get("/api/bookings").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(bookings.size())))
				.andExpect(jsonPath("$[0].dateStart".toString(), is(bookings.get(0).getDateStart().toString())))
				.andExpect(jsonPath("$[0].dateEnd".toString(), is(bookings.get(0).getDateEnd().toString())))
				.andExpect(jsonPath("$[0].roomId", is((int) bookings.get(0).getRoomId())))
				.andExpect(jsonPath("$[0].interventionId", is((int) bookings.get(0).getInterventionId())));

	}

	@Test
	void shouldFectchAllBookingsWithDates() throws Exception {

		LocalDate start = LocalDate.parse("2022-03-31");
		LocalDate end = LocalDate.parse("2022-05-15");

		List<BookingDto> betweenBooking = new ArrayList<>();

		betweenBooking.add(bookings.get(0));
		betweenBooking.add(bookings.get(1));

		when(bookingService.getAllBookings(start, end)).thenReturn(betweenBooking);

		mockMvc.perform(get("/api/bookings/{startDate}/{endDate}", start, end).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(betweenBooking.size())))
				.andExpect(jsonPath("$[0].dateStart".toString(), is(betweenBooking.get(0).getDateStart().toString())))
				.andExpect(jsonPath("$[0].dateEnd".toString(), is(betweenBooking.get(0).getDateEnd().toString())))
				.andExpect(jsonPath("$[0].roomId", is((int) betweenBooking.get(0).getRoomId())))
				.andExpect(jsonPath("$[0].interventionId", is((int) betweenBooking.get(0).getInterventionId())));

	}
	
	@Test
	void shouldGetBookingById() throws Exception{
		
		long id = 1L;
		
		when(bookingService.getById(id)).thenReturn(bookings.get(1));
		
		mockMvc.perform(get("/api/bookings/{id}", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.dateStart".toString(), is(bookings.get(1).getDateStart().toString())))
				.andExpect(jsonPath("$.dateEnd".toString(), is(bookings.get(1).getDateEnd().toString())))
				.andExpect(jsonPath("$.roomId", is((int) bookings.get(1).getRoomId())))
				.andExpect(jsonPath("$.interventionId", is((int) bookings.get(1).getInterventionId())));
				
	}
	
	@Test
	void shouldGetBookingsForRoomId() throws Exception {
		
		long roomId = 2;
		
		List<BookingDto> roomBooking = new ArrayList<>();

		roomBooking.add(bookings.get(1));
		roomBooking.add(bookings.get(3));
		
		
		when(bookingService.getAllBookingsRoom(roomId)).thenReturn(roomBooking);
		
		
		mockMvc.perform(get("/api/bookings/roomBookings/{roomId}", roomId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].dateStart".toString(), is(roomBooking.get(0).getDateStart().toString())))
				.andExpect(jsonPath("$[0].dateEnd".toString(), is(roomBooking.get(0).getDateEnd().toString())))
				.andExpect(jsonPath("$[0].roomId", is((int) roomBooking.get(0).getRoomId())))
				.andExpect(jsonPath("$[0].interventionId", is((int) roomBooking.get(0).getInterventionId())));
	}
	
	
	@Test
	void shouldCountBookingsAndReturnLong() throws Exception {
		
		long count = 5;
		
		when(bookingService.count()).thenReturn(count);
		
		
		mockMvc.perform(get("/api/bookings/count").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(bookings.size())));
		
		
	}
	
	@Test
	void shouldDeleteBookingById() throws Exception {
		
		long id = 5 ;
		
		doNothing().when(bookingService).deleteById(id);		
		
		String result = mockMvc.perform(delete("/api/bookings/{id}", id))
				.andExpect(status().isAccepted())
				.andReturn().getResponse().getContentAsString();
		
		
		assertEquals("Booking with id : " + id + " has been deleted", result);
	}

	
	@Test
	void shouldFailToDeleteId() throws Exception {
		
		long id = 6 ;
			
		doThrow(IllegalArgumentException.class).when(bookingService).deleteById(id);		
		
		String result = mockMvc.perform(delete("/api/bookings/{id}", id))
				.andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		
		
		assertEquals("Booking with id : " + id + " not found", result);
	}
	
	

	@Test
	void shouldUpdateBooking() throws Exception {
		
		BookingDto bookingUpdatedVersion = bookings.get(0).clone();
		bookingUpdatedVersion.setRoomId(10);
		bookingUpdatedVersion.setVersion(bookingUpdatedVersion.getVersion() + 1 );
		
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String bookingJson = objectMapper.writeValueAsString(bookingUpdatedVersion);
		
		when(bookingService.saveOrUpdate(any(BookingDto.class))).thenReturn(bookingUpdatedVersion);
		
		
		mockMvc.perform(put("/api/bookings")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer testTokenForTestingPurpose").content(bookingJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.dateStart".toString(), is(bookingUpdatedVersion.getDateStart().toString())))
				.andExpect(jsonPath("$.dateEnd".toString(), is(bookingUpdatedVersion.getDateEnd().toString())))
				.andExpect(jsonPath("$.roomId", is((int) bookingUpdatedVersion.getRoomId())))
				.andExpect(jsonPath("$.interventionId", is((int) bookingUpdatedVersion.getInterventionId())))
				.andExpect(jsonPath("$.version", is((int) bookingUpdatedVersion.getVersion())));
				
		
//		mockMvc.perform(
//				put("/api/interventions").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
//				.header("Authorization", "Bearer testTokenForTestingPurpose").content(intervJson))
//				.andExpect(status().isOk());
	}
	
	@Test
	void shouldFailToUpdateAndReturnExpectationFailed() throws Exception {
		
		BookingDto bookingUpdatedVersion = bookings.get(0).clone();
		bookingUpdatedVersion.setRoomId(10);
		bookingUpdatedVersion.setId(-1);
		bookingUpdatedVersion.setVersion(bookingUpdatedVersion.getVersion() + 1 );
		
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String bookingJson = objectMapper.writeValueAsString(bookingUpdatedVersion);
		
		mockMvc.perform(put("/api/bookings")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer testTokenForTestingPurpose").content(bookingJson))
				.andExpect(status().isExpectationFailed());
				
		
	}
	
	@Test
	void shouldFailToUpdateAndReturnConflict() throws Exception {
		BookingDto bookingUpdatedVersion = bookings.get(0).clone();
		bookingUpdatedVersion.setRoomId(10);
		bookingUpdatedVersion.setVersion(bookingUpdatedVersion.getVersion() + 1 );
		
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String bookingJson = objectMapper.writeValueAsString(bookingUpdatedVersion);
		
		doThrow(PersistenceException.class).when(bookingService).saveOrUpdate(bookingUpdatedVersion);				
		
		mockMvc.perform(put("/api/bookings")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer testTokenForTestingPurpose").content(bookingJson))
				.andExpect(status().isConflict());
		
	}
	
	@Test
	void shouldFailToUpdateAndReturnNotFound() throws Exception{
		
		BookingDto bookingUpdatedVersion = bookings.get(0).clone();
		bookingUpdatedVersion.setRoomId(10);
		bookingUpdatedVersion.setVersion(bookingUpdatedVersion.getVersion() + 1 );
		
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String bookingJson = objectMapper.writeValueAsString(bookingUpdatedVersion);
		
		when(bookingService.saveOrUpdate(any(BookingDto.class))).thenReturn(null);
		
		
		mockMvc.perform(put("/api/bookings")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer testTokenForTestingPurpose").content(bookingJson))
				.andExpect(status().isNotFound());
	}
	
	
	@Test
	void shouldFailToCreateAndReturnNotAcceptable() throws Exception{
		
		
		BookingDto bookingToCreate = bookings.get(0).clone();
		bookingToCreate.setId(10);
		bookingToCreate.setRoomId(10);
		
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String bookingJson = objectMapper.writeValueAsString(bookingToCreate);
		
		mockMvc.perform(post("/api/bookings")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer testTokenForTestingPurpose").content(bookingJson))
				.andExpect(status().isNotAcceptable());
	}
}
