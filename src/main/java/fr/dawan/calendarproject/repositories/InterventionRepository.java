package fr.dawan.calendarproject.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.enums.InterventionStatus;
import fr.dawan.calendarproject.enums.UserType;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long> {

	@Query("FROM Intervention i LEFT JOIN FETCH i.location LEFT JOIN FETCH i.user WHERE i.course.id = :id")
	List<Intervention> findByCourseId(@Param("id") long id);

	@Query("FROM Intervention i LEFT JOIN FETCH i.course LEFT JOIN FETCH i.location LEFT JOIN FETCH i.user LEFT JOIN FETCH i.course WHERE i.course.title LIKE :title")
	List<Intervention> findByCourseTitle(@Param("title") String title);

	@Query("FROM Intervention i LEFT JOIN FETCH i.user LEFT JOIN FETCH i.location LEFT JOIN FETCH i.user LEFT JOIN FETCH i.course WHERE i.user.id = :userId")
	List<Intervention> findByUserId(@Param("userId") long userId);

	@Query("FROM Intervention i LEFT JOIN FETCH i.user LEFT JOIN FETCH i.location LEFT JOIN FETCH i.user LEFT JOIN FETCH i.course WHERE i.isMaster = false AND i.user.id = :userId AND ((i.dateStart BETWEEN :start AND :end) AND (i.dateEnd BETWEEN :start AND :end))")
	List<Intervention> findByUserIdAndDates(@Param("userId") long userId, @Param("start") LocalDate dateStart,
			@Param("end") LocalDate dateEnd);

	@Query("FROM Intervention i LEFT JOIN FETCH i.user LEFT JOIN FETCH i.location LEFT JOIN FETCH i.user LEFT JOIN FETCH i.course WHERE i.isMaster = false AND i.user.id = :id AND (i.dateStart BETWEEN :start AND :end OR i.dateEnd BETWEEN :start AND :end)")
	List<Intervention> findFromUserByDateRange(@Param("id") long userId, @Param("start") LocalDate dateStart,
			@Param("end") LocalDate dateEnd);

	@Query("FROM Intervention i LEFT JOIN FETCH i.location LEFT JOIN FETCH i.user LEFT JOIN FETCH i.course WHERE i.dateStart BETWEEN :start AND :end OR i.dateEnd BETWEEN :start AND :end")
	List<Intervention> findAllByDateRange(@Param("start") LocalDate dateStart, @Param("end") LocalDate dateEnd);

	// get only master event
	@Query("FROM Intervention i LEFT JOIN FETCH i.location LEFT JOIN FETCH i.user LEFT JOIN FETCH i.course WHERE i.isMaster = true")
	List<Intervention> getMasterIntervention();

	// get events without master (children and orphan) by UserType and between a
	// range of Dates
	@Query("FROM Intervention i LEFT JOIN FETCH i.user LEFT JOIN FETCH i.location LEFT JOIN FETCH i.user LEFT JOIN FETCH i.course WHERE i.isMaster = false AND i.user.type= :type AND (i.dateStart BETWEEN :start AND :end OR i.dateEnd BETWEEN :start AND :end)")
	List<Intervention> getAllChildrenByUserTypeAndDates(@Param("type") UserType type,
			@Param("start") LocalDate dateStart, @Param("end") LocalDate dateEnd);

	@Query("SELECT COUNT(*) FROM Intervention i WHERE i.isMaster = false AND i.user.type= :type")
	long countByUserTypeNoMaster(@Param("type") UserType type);

	List<Intervention> findByMasterInterventionIdOrderByDateStart(long id);

	@Query("FROM Intervention i LEFT JOIN FETCH i.location LEFT JOIN FETCH i.user WHERE i.isMaster = false AND i.user.id = :userId")
	List<Intervention> getAllByUserId(@Param("userId") long userId);

}
