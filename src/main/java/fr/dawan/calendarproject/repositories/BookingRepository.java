package fr.dawan.calendarproject.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Booking;

@Repository	
public interface BookingRepository extends JpaRepository<Booking,Long>  {
	@Query("FROM Booking b WHERE b.beginDate BETWEEN :begin And :ending OR b.endingDate BETWEEN :begin AND :ending")
	List<Booking> findAllByDateRange(@Param("begin") LocalDate beginDate, @Param("ending") LocalDate endingDate);

	@Query("FROM Booking b WHERE b.id IS NOT :id AND b.intervention.id = :interventionId AND b.room.id = :roomId")
	Optional<Booking> findByRoomIdAndInterventionId(@Param("id") long id, @Param("interventionId") long interventionId, @Param("roomId") long roomId);
	
}
