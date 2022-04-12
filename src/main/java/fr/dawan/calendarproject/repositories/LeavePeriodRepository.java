package fr.dawan.calendarproject.repositories;

import fr.dawan.calendarproject.entities.LeavePeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeavePeriodRepository extends JpaRepository<LeavePeriod, Long> {

	@Query("FROM LeavePeriod lp WHERE lp.employeeId = :employeeId ")
	Optional<List<LeavePeriod>> findByUserEmployeeId(@Param("employeeId") long employeeId);

}
