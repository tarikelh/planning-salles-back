package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.dto.BookingDto;
import fr.dawan.calendarproject.entities.Booking;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.mapper.BookingMapper;
import fr.dawan.calendarproject.repositories.BookingRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.RoomRepository;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private BookingMapper bookingMapper;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private InterventionRepository interventionRepository;
	
	@Override
	public List<BookingDto> getAllBookings() {

		List<BookingDto> result = new ArrayList<BookingDto>();
		
		List<Booking> bookings = bookingRepository.findAll();
		for (Booking booking : bookings) {
			
			result.add(bookingMapper.bookingToBookingDto(booking));
		}
		return result;
	}

	@Override
	public List<BookingDto> getAllBookings(LocalDate beginDate, LocalDate endingDate) {
		
		List<BookingDto> result = new ArrayList<BookingDto>();
		
		List<Booking> bookings = bookingRepository.findAllByDateRange(beginDate, endingDate);
		
		for (Booking booking : bookings) {
			
			result.add(bookingMapper.bookingToBookingDto(booking));
		}
		return result;

	}

	@Override
	public BookingDto getById(long id) {
		
		Optional<Booking> booking = bookingRepository.findById(id);
		if(booking.isPresent()) {
			
			return bookingMapper.bookingToBookingDto(booking.get());
		
		}
		
		return null;
	}

	@Override
	public long count() {

		return bookingRepository.count();
	}

	@Override
	public void deleteById(long id) {

		bookingRepository.deleteById(id);
	}

	@Override
	public BookingDto saveOrUpdate(BookingDto bookingDto) {

		checkUniqueness(bookingDto);
		
		if(bookingDto.getId() > 0 && !bookingRepository.findById(bookingDto.getId()).isPresent())
			return null;
		
		Booking b = bookingMapper.bookingDtoTobooking(bookingDto);
		b.setRoom(roomRepository.findById(bookingDto.getRoomId()).orElse(null));
		b.setIntervention(interventionRepository.findById(bookingDto.getInterventionId()).orElse(null));
		
		
		try {
			bookingRepository.saveAndFlush(b);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		return bookingMapper.bookingToBookingDto(b);
	}

	@Override
	public void checkUniqueness(BookingDto bookingDto) {

		Booking duplicate = bookingRepository.findByRoomIdAndInterventionId(bookingDto.getId(), 
																			bookingDto.getInterventionId(), 
																			bookingDto.getRoomId())
																			.orElse(null);
		
		if( duplicate != null ) {
			
			Set<APIError> errors = new HashSet<>();
			String instanceClass = duplicate.getClass().toString();
			errors.add(new APIError(505, instanceClass, "Booking Not Unique",
					"Booking with room name : " + duplicate.getRoom().getName() +
					" or intervention : " + duplicate.getIntervention().getSlug() + 
					" already exists", "/api/bookings"));

			throw new EntityFormatException(errors);
		}
	}

}
