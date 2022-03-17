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
	@Query("FROM Booking b LEFT JOIN FETCH r.room WHERE b.dateStart BETWEEN :start AND :end OR b.dateEnd BETWEEN :start AND :end")
	List<Booking> findAllByDateRange(@Param("start") LocalDate dateStart, @Param("end") LocalDate dateEnd);

}
