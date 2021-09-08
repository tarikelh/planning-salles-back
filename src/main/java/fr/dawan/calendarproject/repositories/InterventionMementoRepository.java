package fr.dawan.calendarproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.InterventionMemento;


@Repository
public interface InterventionMementoRepository extends JpaRepository<InterventionMemento, Long>{

	@Query("SELECT COUNT(*) FROM InterventionMemento i WHERE i.state.interventionId = :interventionId")
	long countByInterventionId(@Param("interventionId") long interventionId);
	
	// get the last intervention memento
	@Query(nativeQuery = true, value = "SELECT * FROM Intervention_Memento i WHERE i.intervention_id = :interventionId ORDER BY i.date_created_state DESC LIMIT 1")
	InterventionMemento getLastInterventionMemento(@Param("interventionId") long interventionId);
}