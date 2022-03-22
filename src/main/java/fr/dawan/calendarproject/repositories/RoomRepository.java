package fr.dawan.calendarproject.repositories;

import fr.dawan.calendarproject.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {

    @Query("FROM Room r WHERE r.location.id = :id AND r.name = :name")
    Room findByLocationIdAndRoomName(@Param("id") long locationId, @Param("name") String name );

    @Query("FROM Room r WHERE r.location.city LIKE :location")
    Page<Room> findAllByLocationNameContaining(@Param("location")String location, Pageable pageable);

    @Query("FROM Room r WHERE r.id IS NOT :id AND r.name = :name")
    Room findByName(@Param("id") long id, @Param("name") String name);

    @Query("FROM Room r WHERE r.isAvailable = true")
    List<Room> findAvailableRoom();

    @Query("FROM Room r Where r.fullCapacity = :capacity")
    List<Room> findByCapacity(@Param("capacity") long capacity);

    @Query("SELECT COUNT(*) FROM Room r WHERE r.location.city LIKE :location ")
    long countByLocationNameContaining(@Param("location")String location);

}
