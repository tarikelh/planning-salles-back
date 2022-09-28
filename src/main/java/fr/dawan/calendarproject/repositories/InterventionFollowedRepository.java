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

	@Query("FROM InterventionFollowed iFollowed LEFT JOIN FETCH iFollowed.student WHERE iFollowed.student.type= :type")
	List<InterventionFollowed> getAllByUserType(@Param("type") UserType type);

	@Query("FROM InterventionFollowed iFollowed "
			+ "LEFT JOIN FETCH iFollowed.student iFolloStud "
			+ "LEFT JOIN FETCH iFollowed.intervention iFolloInterv "
			+ "WHERE iFolloStud.type= :type "
			+ "AND iFolloInterv.dateStart BETWEEN :start AND :end "
			+ "OR iFolloInterv.dateEnd BETWEEN :start AND :end")
	List<InterventionFollowed> getAllByUserTypeAndDateRange(@Param("type") UserType type,
			@Param("start") LocalDate dateStart, @Param("end") LocalDate dateEnd);

	@Query("FROM InterventionFollowed iFollo JOIN iFollo.intervention i "
			+ "WHERE i.dateStart >= :start AND i.dateEnd <= :end")
	List<InterventionFollowed> findAllByDateRange(@Param("start") LocalDate dateStart, @Param("end") LocalDate dateEnd);
	
//	@Query(value= "SELECT * FROM InterventionFollowed iFollo, intervention inter "
//			+ "WHERE inter.id=iFollo.intervention.id"
//			+ "AND (inter.dateStart>=:start) "
//			+ "AND (inter.dateEnd<=:end)",  nativeQuery=true)
//	List<InterventionFollowed> findAllByDateRange(@Param("start") LocalDate dateStart, @Param("end") LocalDate dateEnd);

	Optional<InterventionFollowed> findByRegistrationSlug(String registrationSlug);
}
