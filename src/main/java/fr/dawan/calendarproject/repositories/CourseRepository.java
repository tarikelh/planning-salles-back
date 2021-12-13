package fr.dawan.calendarproject.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

	@Query("FROM Course c WHERE c.id IS NOT :id AND c.title = :title")
	Course findByTitle(@Param("id") long id, @Param("title") String title);

	Optional<Course> findBySlug(String slug);

	Course findByTitleAndDuration(String title, String duration);

	Page<Course> findAllByTitleContaining(String title, Pageable pageable);

	long countByTitleContaining(String title);

}
