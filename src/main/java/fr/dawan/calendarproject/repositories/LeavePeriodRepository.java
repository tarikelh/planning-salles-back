package fr.dawan.calendarproject.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.LeavePeriod;
import fr.dawan.calendarproject.enums.UserType;

@Repository
public interface LeavePeriodRepository extends JpaRepository<LeavePeriod, Long> {

	@Query("FROM LeavePeriod lp LEFT JOIN FETCH lp.user u WHERE u.employeeIdDg2= :employeeId ")
	List<LeavePeriod> findByUserEmployeeId(@Param("employeeId") long employeeId);

	@Query("FROM LeavePeriod lp LEFT JOIN FETCH lp.user u LEFT JOIN FETCH u.skills LEFT JOIN FETCH u.location WHERE u.type=:type AND lp.firstDay > :start AND lp.lastDay < :end")
	List<LeavePeriod> getAllByUserTypeAndDates(@Param("type") UserType type,
			@Param("start") LocalDate dateStart, @Param("end") LocalDate dateEnd);
	
	@Query("FROM LeavePeriod lp "
			+ "LEFT JOIN FETCH lp.user u "
			+ "LEFT JOIN FETCH u.skills "
			+ "LEFT JOIN FETCH u.location "
			+ "WHERE u.id = :userId "
			+ "AND lp.firstDay <= :end "
            + "AND lp.lastDay >= :start")
	List<LeavePeriod> getAllOverlapingByUserIdAndDates(@Param("userId") long userId, @Param("start") LocalDate dateStart, @Param("end") LocalDate dateEnd);

	@Query("SELECT COUNT(*) FROM LeavePeriod i WHERE i.user.type= :type")
	long countByUserType(UserType type);

}
