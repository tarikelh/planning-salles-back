package fr.dawan.calendarproject.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

	@Query("FROM Skill s LEFT JOIN FETCH s.users")
	List<Skill> findAll();
}
