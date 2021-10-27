package fr.dawan.calendarproject.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.InterventionMemento;


@Repository
public interface InterventionMementoRepository extends JpaRepository<InterventionMemento, Long>{
	
	List<InterventionMemento> findAllByOrderByIdDesc(Pageable pageable);

	@Query("SELECT COUNT(*) FROM InterventionMemento i WHERE i.state.interventionId = :interventionId")
	long countByInterventionId(@Param("interventionId") long interventionId);
	
	// get the last intervention memento to compare with
	@Query(nativeQuery = true, value = "SELECT * FROM intervention_memento i WHERE i.intervention_id = :interventionId ORDER BY i.date_created_state DESC LIMIT 1")
	InterventionMemento getLastInterventionMemento(@Param("interventionId") long interventionId);
	
	@Query("FROM InterventionMemento i WHERE i.state.interventionId = :interventionId AND (i.dateCreatedState BETWEEN :start AND :end)")
	List<InterventionMemento> filterByIntIdAndDates(@Param("interventionId") long interventionId, @Param("start") LocalDateTime dateStart, @Param("end") LocalDateTime dateEnd, Pageable pageable);
	
	@Query("SELECT COUNT(*) FROM InterventionMemento i WHERE i.state.interventionId = :interventionId AND (i.dateCreatedState BETWEEN :start AND :end)")
	long countFilter(@Param("interventionId") long interventionId, @Param("start") LocalDateTime dateStart, @Param("end") LocalDateTime dateEnd);
	
	// get the "last before" intervention memento to visualize modifications that were done
	@Query(nativeQuery = true, value = "SELECT * FROM intervention_memento i WHERE i.intervention_id = :interventionId AND i.id < :interventionMementoId ORDER BY i.id DESC LIMIT 1")
	InterventionMemento getLastBeforeIntMemento(@Param("interventionId") long interventionId, @Param("interventionMementoId") long interventionMementoId);
}