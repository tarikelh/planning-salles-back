package fr.dawan.calendarproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("FROM User u WHERE u.mail = :mail")
	User findByEmail(@Param("mail") String mail);
}
