package fr.dawan.calendarproject.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Resource;

@Repository	
public interface ResourceRepository extends JpaRepository<Resource,Long> {

	 @Query("FROM Resource r WHERE r.name LIKE :name")
	 List<Resource> findByName(@Param("name") String name);
	 
	 @Query("FROM Resource r WHERE r.id IS NOT :id AND (r.name = :name OR r.roomId = :roomId)")
	 List<Resource> findDuplicateByName(@Param("id") long id, @Param("name") String name, @Param("roomId") long roomId );
	 
	 @Query("FROM Resource r WHERE r.room.isAvailable = :availability")
	 List<Resource> findByRoomAvailability(@Param("availability") boolean  availability);
	 
	 @Query("FROM Resource r WHERE r.quantity = :quantity")
	 List<Resource> findByQuantity(@Param("quantity") int quantity);
	 
	 //List<Resource> findByQuantityContaining(int quantity);
	 
	 @Query("FROM Resource r WHERE r.quantity BETWEEN :value1 AND :value2")
	 List<Resource> findByQuantityRange(@Param("value1") int value1, @Param("value2") int value2);
	 
	 Page<Resource> findAllByNameContaining(String name, Pageable pageable);
	 
	 long countByNameContaining(String name);
	 

}
