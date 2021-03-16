package fr.dawan.calendarproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

}
