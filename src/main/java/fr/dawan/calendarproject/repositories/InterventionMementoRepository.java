package fr.dawan.calendarproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.InterventionMemento;


@Repository
public interface InterventionMementoRepository extends JpaRepository<InterventionMemento, Long>{

	
}
