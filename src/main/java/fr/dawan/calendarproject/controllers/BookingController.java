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
import fr.dawan.calendarproject.services.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	@Autowired
	private BookingService bookingService; 
	
	
	//GET
	@GetMapping(produces = "application/json")
	public List<BookingDto> getAll(){
		
		return bookingService.getAllBookings();
	}
	
	//GET
	@GetMapping(value ="/{startDate}/{endDate}", produces = "application/json")
	public List<BookingDto> getAllWithDates(@PathVariable("startDate") String start, @PathVariable("endDate") String end){
		
		return bookingService.getAllBookings(LocalDate.parse(start), LocalDate.parse(end));
		
	}
	
	//GET
	@GetMapping(value="/{id}", produces="application/json")
	public BookingDto getBookingById(@PathVariable("id") long id) {
		
		return bookingService.getById(id);
	}
	
	//GET
	@GetMapping(value="/roomBookings/{id}", produces="application/json")
	public List<BookingDto> getBookingsForRoomId(@PathVariable("id") long roomId){
		
		return bookingService.getAllBookingsRoom(roomId);
	}
	
	//GET
	@GetMapping(value="/count")
	public long getNumberOfBookings() {
		
		return bookingService.count();
	}
	
	
	//DELETE
	@DeleteMapping(value="/{id}")
	public ResponseEntity<String> deleteBooking(@PathVariable("id") long id){
		
		try {
			bookingService.deleteById(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Booking with id : " + id + " has been deleted");
			
		} catch (Exception e) {
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking with id : " + id + " not found");
		}
	}
	
	//PUT
	@PutMapping(consumes="application/json", produces="application/json")
	public ResponseEntity<Object> updateBooking(@RequestBody BookingDto bookingDto){
		
		
		if(bookingDto.getId() > 0) {
			

			BookingDto response = null;
			try {
				response = bookingService.saveOrUpdate(bookingDto);
			} catch (Exception e) {

				return ResponseEntity.status(HttpStatus.CONFLICT).body("Dates conflit : the tharget date range is not available");
			}
			 
			if(response != null) {
				return ResponseEntity.status(HttpStatus.OK).body(response);
			
			} else {
				
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking with id : " + bookingDto.getId() + " not found");
			}
		}else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Id must be superior than 0. Are you trying to update ? If so use POST method instead");
		}
		
	}
	
	
	//POST
	@PostMapping(consumes="application/json", produces="application/json")
	public ResponseEntity<Object> createBooking(@RequestBody BookingDto bookingDto){
		
		
		if(bookingDto.getId() == 0) {
			
					
			BookingDto response = null;
			try {
				response = bookingService.saveOrUpdate(bookingDto);
			} catch (Exception e) {

				return ResponseEntity.status(HttpStatus.CONFLICT).body("Dates conflit : the tharget date range is not available");
			}
			
			if(response != null) {
				
				return ResponseEntity.status(HttpStatus.CREATED).body(response);

			} else {
				
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Booking hasn't been created");
			}
		} else {
			
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Booking hasn't been created, id must be 0. Are you trying to update ? If so use PUT method instead");
		}
			
		
		
		
	}
	
}	

