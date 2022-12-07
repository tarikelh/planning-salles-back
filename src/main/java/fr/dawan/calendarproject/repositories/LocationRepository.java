package fr.dawan.calendarproject.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

	@Query("FROM Location l WHERE l.id IS NOT :id AND (l.color = :color OR l.city = :city)")
	List<Location> findDuplicates(@Param("id") long id, @Param("city") String city, @Param("color") String color);

	Page<Location> findAllByCityContaining(String city, Pageable pageable);

	long countByCityContaining(String city);

	Optional<Location> findByCity(String city);

	Optional<Location> findByIdDg2(long locationId);

//	@Query("FROM Location l WHERE l.published=true")
//	List<Location> findAllPublished();
	
	@Query("FROM Location l")
	List<Location> findAllPublished();
}
