package fr.dawan.calendarproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

}
