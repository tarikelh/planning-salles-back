package fr.dawan.calendarproject.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Intervention;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long> {
	
    @Query("FROM Intervention i WHERE i.course.id = :id")
    List<Intervention> findByCourseId(@Param("id") long id);

    @Query("FROM Intervention i WHERE i.course.title LIKE :title")
    List<Intervention> findByCourseTitle(@Param("title") String title);
}
