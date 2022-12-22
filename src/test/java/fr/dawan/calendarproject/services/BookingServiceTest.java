package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import fr.dawan.calendarproject.dto.BookingDto;
import fr.dawan.calendarproject.entities.Booking;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.Room;
import fr.dawan.calendarproject.mapper.BookingMapper;
import fr.dawan.calendarproject.repositories.BookingRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class BookingServiceTest {

	@Autowired
	private BookingService bookingService;
	
	@MockBean
	private BookingRepository bookingRepository;
	
	@MockBean
	private BookingMapper bookingMapper;
	
	private List<Booking> bList = new ArrayList<>();
	private List<BookingDto> bDtoList = new ArrayList<>();
	private List<Booking> bListBetween = new ArrayList<>();
	private List<BookingDto> bDtoListBetween = new ArrayList<>();
	private List<Booking> bListRoom = new ArrayList<>();
	private List<BookingDto> bDtoListRoom = new ArrayList<>();
	private Booking booking1; 
	private Booking booking2; 
	private Booking booking3;
	private Booking booking4;
	private Booking booking5;
	private BookingDto bookingDto1;
	private BookingDto bookingDto2;
	private BookingDto bookingDto3;
	private BookingDto bookingDto4;
	private BookingDto bookingDto5;
	private Optional<Booking> optional;
	
	
	private long count;
	private Room room1 = new Room();
	private Room room2 = new Room();
	private Intervention int1 = new Intervention();
	private Intervention int2 = new Intervention();
	private Intervention int3 = new Intervention();
	private LocalDate start;
	private LocalDate end;
	
	
	@BeforeEach
	void beforeEach() throws Exception{
		
		room1.setId(1);
		room2.setId(2);
		
		int1.setId(1);
		int2.setId(2);
		int3.setId(3);
		
		start = LocalDate.parse("2022-04-01");
		end = LocalDate.parse("2022-05-15");
		
		count = 5;

		booking1 = new Booking( 1, room1, int1, LocalDate.parse("2022-04-01"), LocalDate.parse("2022-04-10"), 0 );
		booking2 = new Booking( 2, room2, int2, LocalDate.parse("2022-05-01"), LocalDate.parse("2022-05-10"), 0 );
		booking3 = new Booking( 3, room1, int3, LocalDate.parse("2022-06-01"), LocalDate.parse("2022-06-10"), 0 );
		booking4 = new Booking( 4, room2, int1, LocalDate.parse("2022-07-01"), LocalDate.parse("2022-07-10"), 0 );
		booking5 = new Booking( 5, room1, int2, LocalDate.parse("2022-08-01"), LocalDate.parse("2022-08-10"), 0 );
		
		
		bookingDto1 = new BookingDto( 1, LocalDate.parse("2022-04-01"), LocalDate.parse("2022-04-10"), 1, 1, 0 );
		bookingDto2 = new BookingDto( 2, LocalDate.parse("2022-05-01"), LocalDate.parse("2022-05-10"), 2, 2, 0 );
		bookingDto3 = new BookingDto( 3, LocalDate.parse("2022-06-01"), LocalDate.parse("2022-06-10"), 1, 3, 0 );
		bookingDto4 = new BookingDto( 4, LocalDate.parse("2022-07-01"), LocalDate.parse("2022-07-10"), 2, 1, 0 );
		bookingDto5 = new BookingDto( 5, LocalDate.parse("2022-08-01"), LocalDate.parse("2022-08-10"), 1, 2, 0 );
		
		
		optional = Optional.of(booking1);

		
		bList.add(booking1);
		bList.add(booking2);
		bList.add(booking3);
		bList.add(booking4);
		bList.add(booking5);
		
		bDtoList.add(bookingDto1);
		bDtoList.add(bookingDto2);
		bDtoList.add(bookingDto3);
		bDtoList.add(bookingDto4);
		bDtoList.add(bookingDto5);

		
		bListBetween.add(booking1);
		bListBetween.add(booking2);
				
		bDtoListBetween.add(bookingDto1);
		bDtoListBetween.add(bookingDto2);
		
		
		bListRoom.add(booking1);
		bListRoom.add(booking3);
		bListRoom.add(booking5);

		bDtoListRoom.add(bookingDto1);
		bDtoListRoom.add(bookingDto3);
		bDtoListRoom.add(bookingDto5);
		
	}
	
	

	@Test
	void contextLoads() {
		assertThat(bookingService).isNotNull();
	}
	
	
	@Test
	void shouldGetBookingsAndReturnDtos() {
		
		//Mocking
		//--repo
		when(bookingRepository.findAll()).thenReturn(bList);
		//--mapper
		when(bookingMapper.bookingToBookingDto(any(Booking.class))).thenReturn(bDtoList.get(0), bDtoList.get(1),bDtoList.get(2), bDtoList.get(3), bDtoList.get(4));
//		when(bookingMapper.bookingToBookingDto(bList.get(1))).thenReturn(bDtoList.get(1));
//		when(bookingMapper.bookingToBookingDto(bList.get(2))).thenReturn(bDtoList.get(2));
//		when(bookingMapper.bookingToBookingDto(bList.get(3))).thenReturn(bDtoList.get(3));
//		when(bookingMapper.bookingToBookingDto(bList.get(4))).thenReturn(bDtoList.get(4));
//		
		//Function Execution
		List<BookingDto> result = bookingService.getAllBookings();
		
		//Assert
		assertThat(bDtoList).containsAll(result);
		
	}
	
	@Test
	void shouldGetBookingsWithinGivenDatesAndReturnDtos() {
		
		//Mocking
		//--repo
		when(bookingRepository.findAllByDateRange(start, end)).thenReturn(bListBetween);
		//--mapper
		when(bookingMapper.bookingToBookingDto(bListBetween.get(0))).thenReturn(bDtoListBetween.get(0));
		when(bookingMapper.bookingToBookingDto(bListBetween.get(1))).thenReturn(bDtoListBetween.get(1));
		
		//Function Execution
		List<BookingDto> result = bookingService.getAllBookings(start, end);
		
		//Assert
		assertThat(bDtoListBetween).containsAll(result);	
	
	}
	
	@Test
	void shouldGetBookingsForGivenRoomAndReturnDtos() {
		
		//Mocking
		//-- repo
		when(bookingRepository.findAllBookingsWithRoomIdExcludeBookingId(room1.getId(), 0)).thenReturn(bListRoom);
		//-- mapper
		when(bookingMapper.bookingToBookingDto(bListRoom.get(0))).thenReturn(bDtoListRoom.get(0));
		when(bookingMapper.bookingToBookingDto(bListRoom.get(1))).thenReturn(bDtoListRoom.get(1));
		when(bookingMapper.bookingToBookingDto(bListRoom.get(2))).thenReturn(bDtoListRoom.get(2));
		
		//Function execution
		List<BookingDto> result = bookingService.getAllBookingsRoom(room1.getId());
		
		//Assert
		assertThat(bDtoListRoom).containsAll(result);
		
	}
	
	@Test
	void shouldGetBookingsForGivenIdAndReturnDto() {
		
		//Mocking
		//--repo
		when(bookingRepository.findById(booking1.getId())).thenReturn(optional);
		//--mapper
		when(bookingMapper.bookingToBookingDto(booking1)).thenReturn(bookingDto1);
		//Function Execution
		
		BookingDto result = bookingService.getById(booking1.getId());
		
		//Assert 
		
		assertEquals(bookingDto1, result);
	}
	
	@Test
	void shouldFailToFindBooking() {
		
		// Mocking
		when(bookingRepository.findById(any(Long.class))).thenReturn(null);
	
		// Function execution		
		BookingDto result = bookingService.getById(6);
		
		// Assert
		assertThat(result).isNull();
		
	}
	
	
	@Test
	void shouldCountBookingsAndReturnLong() {
		
		
		//Mocking
		
		when(bookingRepository.count()).thenReturn(count);
		
		//Function execution
		
		long result = bookingService.count();
		
		//Assert
		
		assertEquals(count, result);
		
	}


	@Test
	void shouldDeleteCourse() throws Exception {

		doNothing().when(bookingRepository).deleteById(any(Long.class));

		assertDoesNotThrow(() -> bookingService.deleteById(any(Long.class)));
	}
	
	@Test
	void testSaveNewBooking() throws Exception {
		BookingDto toCreate = new BookingDto(0,  LocalDate.parse("2022-04-01"), LocalDate.parse("2022-04-10"), 1, 1, 0);
		Booking repoReturn = new Booking(5, room1, int1, LocalDate.parse("2022-04-01"), LocalDate.parse("2022-04-10"), 0);
		BookingDto expected = new BookingDto(5,  LocalDate.parse("2022-04-01"), LocalDate.parse("2022-04-10"), 1, 1, 0);

		when(bookingMapper.bookingDtoTobooking(any(BookingDto.class))).thenReturn(repoReturn);
		when(bookingRepository.saveAndFlush(any(Booking.class))).thenReturn(repoReturn);
		when(bookingMapper.bookingToBookingDto(any(Booking.class))).thenReturn(expected);

		BookingDto result = bookingService.saveOrUpdate(toCreate);

		assertThat(result).isNotNull();
		assertEquals(expected, result);
	}
	
	/*@Test
	void shouldThrowWhenCheckBookingRangeEmpty () throws Exception {
		BookingDto toCreate1 = new BookingDto(6,  LocalDate.parse("2022-04-01"), LocalDate.parse("2022-04-10"), 1, 1, 0);
		BookingDto toCreate2 = new BookingDto(6,  LocalDate.parse("2022-04-01"), LocalDate.parse("2022-04-10"), 1, 1, 0);
		Booking repoReturn = new Booking(6, room1, int1,  LocalDate.parse("2022-04-01"), LocalDate.parse("2022-04-10"), 0);
		BookingDto expected = new BookingDto(6,  LocalDate.parse("2022-04-01"), LocalDate.parse("2022-04-10"), 1, 1, 0);

		when(bookingMapper.bookingDtoTobooking(any(BookingDto.class))).thenReturn(repoReturn);
		when(bookingRepository.saveAndFlush(any(Booking.class))).thenReturn(repoReturn);
		when(bookingMapper.bookingToBookingDto(any(Booking.class))).thenReturn(expected);

		BookingDto result1 = bookingService.saveOrUpdate(toCreate1);
		BookingDto result2 = bookingService.saveOrUpdate(toCreate2);

		assertThrows(Exception.class, () -> {
			bookingService.saveOrUpdate(result1);
			bookingService.saveOrUpdate(result2);
		});
	}*/

}
