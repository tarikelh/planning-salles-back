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

	/**
	 * Handles HTTP GET requests to retrieve a list of all bookings.
	 *
	 * @return a list of {@link BookingDto} objects
	 */
	@GetMapping(produces = "application/json")
	public List<BookingDto> getAll() {
		return bookingService.getAllBookings();
	}

	/**
	 * Handles HTTP GET requests to retrieve a list of bookings within a specific
	 * date range.
	 *
	 * @param start the start date of the range, in the format "yyyy-MM-dd"
	 * @param end   the end date of the range, in the format "yyyy-MM-dd"
	 * @return a list of {@link BookingDto} objects
	 */
	@GetMapping(value = "/{startDate}/{endDate}", produces = "application/json")
	public List<BookingDto> getAllWithDates(@PathVariable("startDate") String start,
			@PathVariable("endDate") String end) {
		return bookingService.getAllBookings(LocalDate.parse(start), LocalDate.parse(end));

	}

	/**
	 * Handles HTTP GET requests to retrieve a specific booking by its ID.
	 *
	 * @param id the ID of the booking
	 * @return the {@link BookingDto} object with the specified ID
	 */
	@GetMapping(value = "/{id}", produces = "application/json")
	public BookingDto getBookingById(@PathVariable("id") long id) {
		return bookingService.getById(id);
	}

	/**
	 * Handles HTTP GET requests to retrieve a list of bookings for a specific room
	 * ID.
	 *
	 * @param roomId the ID of the room
	 * @return a list of {@link BookingDto} objects for the specified room ID
	 */
	@GetMapping(value = "/roomBookings/{id}", produces = "application/json")
	public List<BookingDto> getBookingsForRoomId(@PathVariable("id") long roomId) {
		return bookingService.getAllBookingsRoom(roomId);
	}

	/**
	 * Handles HTTP GET requests to retrieve the number of bookings.
	 *
	 * @return the number of bookings
	 */
	@GetMapping(value = "/count")
	public long getNumberOfBookings() {
		return bookingService.count();
	}

	/**
	 * Handles HTTP DELETE requests to delete a specific booking by its ID.
	 *
	 * @param id the ID of the booking
	 * @return a {@link ResponseEntity} with an appropriate status code and message
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> deleteBooking(@PathVariable("id") long id) {
		StringBuffer message = new StringBuffer();
		HttpStatus status;

		message.append("Booking with id : ").append(id);

		if (bookingService.deleteById(id)) {
			message.append(" has been deleted");
			status = HttpStatus.ACCEPTED;
		} else {
			message.append(" not found");
			status = HttpStatus.NOT_FOUND;
		}

		return ResponseEntity.status(status).body(message.toString());
	}

	/**
	 * 
	 * Updates the booking with the specified ID.
	 * 
	 * @param bookingDto the booking to update
	 * @return the updated booking if successful, or a message describing the error
	 *         that occurred
	 */
	@PutMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> updateBooking(@RequestBody BookingDto bookingDto) {
		Object message;
		HttpStatus status;

		if (bookingDto.getId() > 0) {

			BookingDto response = null;
			try {
				response = bookingService.saveOrUpdate(bookingDto);
			} catch (Exception e) {
				status = HttpStatus.CONFLICT;
				message = "Dates conflit : the tharget date range is not available";
			}

			if (response != null) {
				status = HttpStatus.OK;
				message = response;
			} else {
				status = HttpStatus.NOT_FOUND;
				message = "Booking with id : " + bookingDto.getId() + " not found";
			}
		} else {
			status = HttpStatus.EXPECTATION_FAILED;
			message = "Id must be superior than 0. Are you trying to update ? If so use POST method instead";
		}
		return ResponseEntity.status(status).body(message);
	}

	/**
	 * 
	 * Creates a new booking.
	 * 
	 * @param bookingDto the booking data transfer object
	 * @return the created booking if successful, or a message describing the error
	 *         that occurred
	 * 
	 */
	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> createBooking(@RequestBody BookingDto bookingDto) {
		Object message;
		HttpStatus status;

		if (bookingDto.getId() == 0) {

			BookingDto response = null;
			try {
				response = bookingService.saveOrUpdate(bookingDto);
			} catch (Exception e) {
				status = HttpStatus.CONFLICT;
				message = "Dates conflit : the tharget date range is not available";
			}
			if (response != null) {
				status = HttpStatus.CREATED;
				message = response;

			} else {
				status = HttpStatus.EXPECTATION_FAILED;
				message = "Booking hasn't been created";
			}
		} else {
			status = HttpStatus.NOT_ACCEPTABLE;
			message = "Booking hasn't been created, id must be 0. Are you trying to update ? If so use PUT method instead";
		}
		return ResponseEntity.status(status).body(message);
	}

}
