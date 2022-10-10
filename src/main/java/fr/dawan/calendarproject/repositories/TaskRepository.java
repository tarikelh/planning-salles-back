package fr.dawan.calendarproject.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Task;
import fr.dawan.calendarproject.enums.UserType;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

	List<Task> findAll();
	
	Optional<Task> findById(long id);
	
	Optional<Task> findByTaskIdDg2(long taskIdDg2);
	
	List<Task> findByUserId(long userId);
	
	List<Task> findByInterventionId(long interventionId);
	
	@Query("FROM Task t WHERE t.beginDate BETWEEN :start AND :end OR t.endDate BETWEEN :start AND :end")
	List<Task> getAllBetweenDates(@Param("start") LocalDate dateStart, @Param("end") LocalDate dateEnd);
	
	@Query("FROM Task t WHERE (t.beginDate BETWEEN :start AND :end OR t.endDate BETWEEN :start AND :end) AND t.user.id = :userId")
	List<Task> getAllByUserIdBetweenDates(@Param("start") LocalDate dateStart, @Param("end") LocalDate dateEnd, @Param("userId") long userId);
	
	List<Task> findByUserType(UserType type);
	
	List<Task> findAllBySlugContainingOrTitleContaining(String slug, String title);
	
	Optional<Task> findBySlug(String slug);
	
}
