package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.List;

import fr.dawan.calendarproject.dto.BookingDto;

public interface BookingService {
	
	
	List<BookingDto> getAllBookings(); 

	List<BookingDto> getAllBookings(LocalDate beginDate, LocalDate endingDate);
	
	List<BookingDto> getAllBookingsRoom(long roomId);
	
	boolean checkBookingRangeEmpty(List<LocalDate> bookingRange, List<LocalDate> bookingsRange);
	
	BookingDto getById(long id);
	
	long count();
	
	void deleteById(long id);
	
	BookingDto saveOrUpdate(BookingDto bookingDto) throws Exception;
	
	
	
}
