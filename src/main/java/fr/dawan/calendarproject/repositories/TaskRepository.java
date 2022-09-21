package fr.dawan.calendarproject.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

	List<Task> findAll();
	
	Optional<Task> findById(long id);
	
	List<Task> findByUserId(long userId);
	
	List<Task> findByInterventionId(long interventionId);
	
	@Query("FROM Task t WHERE t.beginDate BETWEEN :start AND :end OR t.endDate BETWEEN :start OR :end")
	List<Task> getAllBetweenDates(@Param("start") LocalDate dateStart, @Param("end") LocalDate dateEnd);
	
	List<Task> findAllBySlugContaining(String slug);
	
	
}
