package fr.dawan.calendarproject.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.dawan.calendarproject.dto.BookingDto;
import fr.dawan.calendarproject.services.BookingServiceImpl;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	@Autowired
	private BookingServiceImpl bookingServiceImpl; 
	
	
	//GET
	@GetMapping(produces = "application/json")
	public List<BookingDto> getAll(){
		
		return bookingServiceImpl.getAllBookings();
	}
	
	//GET
	@GetMapping(value ="/{startDate}/{endDate}", produces = "application/json")
	public List<BookingDto> getAllWithDates(@PathVariable("startDate") String start, @PathVariable("endDate") String end){
		
		return bookingServiceImpl.getAllBookings(LocalDate.parse(start), LocalDate.parse(end));
		
	}
	
	//GET
	@GetMapping(value="/{id}", produces="application/json")
	public BookingDto getBookingById(@PathVariable("id") long id) {
		
		return bookingServiceImpl.getById(id);
	}
	
	//GET
	@GetMapping(value="/count")
	public long getNumberOfBookings() {
		
		return bookingServiceImpl.count();
	}
	
	
	//DELETE
	@DeleteMapping(value="/{id}")
	public ResponseEntity<String> deleteBooking(@PathVariable("id") long id){
		
		try {
			bookingServiceImpl.deleteById(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Booking with id : " + id + " has been deleted");
			
		} catch (Exception e) {
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking with id : " + id + " not found");
		}
	}
	
	//PUT
	@PutMapping(consumes="application/json", produces="application/json")
	public ResponseEntity<Object> updateBooking(@RequestBody BookingDto bookingDto){
		
		
		if(bookingDto.getId() > 0) {
			
			// TODO Check if bookings already exists with different Id
			// TODO Check if there is no conflit for selected room

			BookingDto response = bookingServiceImpl.saveOrUpdate(bookingDto);
			
			if(response != null) {
				return ResponseEntity.status(HttpStatus.OK).body(response);
			
			} else {
				
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking with id : " + bookingDto.getId() + " not found");
			}
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id must be superior than 0. Are you trying to update ? If so use POST method instead");
		}
		
	}
	
	
	//POST
	@PostMapping(consumes="application/json", produces="application/json")
	public ResponseEntity<Object> createBooking(@RequestBody BookingDto bookingDto){
		
		
		if(bookingDto.getId() == 0) {
			
			// TODO Check if booking doesn't already exists 
			// TODO Check if there is no conflit for selected room
			
			BookingDto response = bookingServiceImpl.saveOrUpdate(bookingDto);
			
			if(response != null) {
				
				return ResponseEntity.status(HttpStatus.CREATED).body(response);

			} else {
				
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Booking hasn't been created");
			}
		} else {
			
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Booking hasn't been created, id must be 0. Are you trying to update ? If so use PUT method instead");
		}
			
		
		
		
	}
	
}	

