package fr.dawan.calendarproject.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.UserType;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.skills LEFT JOIN FETCH u.location ORDER BY u.lastName ASC")
	List<User> findAll();

	long countByFirstNameContainingOrLastNameContainingOrEmailContaining(String firstName, String lastName,
			String email);

	Page<User> findAllByFirstNameContainingOrLastNameContainingOrEmailContaining(String firstName, String lastName,
			String email, Pageable pageable);

	@Query("FROM User u WHERE u.email = :email")
	Optional<User> findByEmail(@Param("email") String email);
	
	@Query("FROM User u WHERE u.employeeIdDg2 = :employeeId")
	Optional<User> findByEmployeeIdDg2(@Param("employeeId") long employeeId);

	@Query("FROM User u WHERE u.email = :email AND u.id <> :id")
	User findDuplicateEmail(@Param("email") String email, @Param("id") long id);

	// Verifier si le le group by id pose PB ?
	@Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.location LEFT JOIN FETCH u.skills s WHERE u.type = :type ORDER BY u.lastName ASC")
	List<User> findAllByType(@Param("type") UserType type);

	Optional<User> findByIdDg2(long userId);
	
	// Verifier si le le group by id pose PB ?
	@Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.location LEFT JOIN FETCH u.skills s WHERE u.type = :type AND u.endDate IS NULL ORDER BY u.lastName ASC")
	List<User> findAllByTypeAndEndDateIsNull(@Param("type") UserType type);

}
