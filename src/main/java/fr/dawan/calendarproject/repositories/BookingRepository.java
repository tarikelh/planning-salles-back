package fr.dawan.calendarproject.repositories;

import java.time.LocalDate;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Booking;

@Repository	
public interface BookingRepository extends JpaRepository<Booking,Long>  {
	@Query("FROM Booking b WHERE b.beginDate BETWEEN :begin And :ending OR b.endingDate BETWEEN :begin AND :ending")
	List<Booking> findAllByDateRange(@Param("begin") LocalDate beginDate, @Param("ending") LocalDate endingDate);
	
	
	// Si on souhaite récupérer l'ensemble des bookings associés au roomId, on passer bookingId à 0
	@Query("FROM Booking b WHERE b.room.id = :roomId AND b.id IS NOT :bookingId")
	List<Booking> findAllBookingsWithRoomIdExcludeBookingId(@Param("roomId") long roomId, @Param("bookingId") long bookingId );
}
