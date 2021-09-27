package fr.dawan.calendarproject.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

	@Query("FROM Skill s LEFT JOIN FETCH s.users")
	List<Skill> findAll();
	
	Page<Skill> findAllByTitleContaining(String title, Pageable pageable);
	
	long countByTitleContaining(String title);
}
