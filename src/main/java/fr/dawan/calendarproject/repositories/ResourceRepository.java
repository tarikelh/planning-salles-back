package fr.dawan.calendarproject.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Resource;

@Repository	
public interface ResourceRepository extends JpaRepository<Resource,Long> {

	 @Query("FROM Resource r WHERE r.name = :name")
	    List<Resource> findByName(@Param("name") String name);
	 

}
