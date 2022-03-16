package fr.dawan.calendarproject.repositories;

import fr.dawan.calendarproject.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Long> {

    @Query("FROM Room r LEFT JOIN FETCH r.location WHERE r.location.city = :location")
    List<Room> findByLocation(@Param("location") String location);

    @Query("FROM Room r WHERE r.isAvailable = true")
    List<Room> findAvailableRoom();

    @Query("FROM Room r Where r.fullCapacity = :capacity ")
    List<Room> findByCapacity(@Param("capacity") long capacity);

    long countByLocation(String location);

}
