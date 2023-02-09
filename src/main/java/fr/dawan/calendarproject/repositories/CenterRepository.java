package fr.dawan.calendarproject.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Center;

@Repository
public interface CenterRepository extends JpaRepository<Center, Long>{
 
	List<Center> findAll();
}
