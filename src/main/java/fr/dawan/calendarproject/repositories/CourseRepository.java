package fr.dawan.calendarproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}
