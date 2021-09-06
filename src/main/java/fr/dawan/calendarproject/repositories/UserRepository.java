package fr.dawan.calendarproject.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.UserType;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("FROM User u WHERE u.email = :email")
	User findByEmail(@Param("email") String email);

	@Query("FROM User u WHERE u.email = :email AND u.id <> :id")
	User findDuplicateEmail(@Param("email") String email, @Param("id") long id);

	@Query(value = "FROM User u LEFT JOIN FETCH u.location WHERE u.type = :type")
	List<User> findAllByType(@Param("type") UserType type);
}
