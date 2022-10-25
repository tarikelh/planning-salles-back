package fr.dawan.calendarproject.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.InterventionFollowed;
import fr.dawan.calendarproject.enums.UserType;

@Repository
public interface InterventionFollowedRepository extends JpaRepository<InterventionFollowed, Long> {

	List<InterventionFollowed> findByUserType(@Param("type") UserType type);	
	
	@Query("FROM InterventionFollowed iFollowed "
			+ "LEFT JOIN FETCH iFollowed.user u "
			+ "LEFT JOIN FETCH iFollowed.intervention i "
			+ "WHERE u.type= :type "
			+ "AND i.dateStart >= :start "
			+ "AND i.dateEnd <= :end")
	List<InterventionFollowed> getAllByUserTypeAndDateRange(@Param("type") UserType type,
			@Param("start") LocalDate dateStart, @Param("end") LocalDate dateEnd);
	
	   @Query("FROM InterventionFollowed iFollowed "
	            + "LEFT JOIN FETCH iFollowed.user u "
	            + "LEFT JOIN FETCH iFollowed.intervention i "
	            + "WHERE u.id= :id "
	            + "AND i.dateStart >= :start "
	            + "AND i.dateEnd <= :end")
	    List<InterventionFollowed> getAllByUserIdAndDateRange(@Param("id") long userId,
	            @Param("start") LocalDate dateStart, @Param("end") LocalDate dateEnd);

	@Query("FROM InterventionFollowed iFollowed "
			+ "LEFT JOIN iFollowed.intervention i "
			+ "WHERE i.dateStart >= :start "
			+ "AND i.dateEnd <= :end")
	List<InterventionFollowed> findAllByDateRange(@Param("start") LocalDate dateStart, @Param("end") LocalDate dateEnd);

	Optional<InterventionFollowed> findByRegistrationSlug(String registrationSlug);
	
    List<InterventionFollowed> findAllByRegistrationSlugContaining(String registrationSlug);
}
