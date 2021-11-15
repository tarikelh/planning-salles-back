package fr.dawan.calendarproject.repositories;

import java.util.List;
import java.util.Map;

import fr.dawan.calendarproject.entities.Intervention;

public interface InterventionCustomRepository {
	
	/*
	// filter for Android
		@Query("FROM Intervention i LEFT JOIN FETCH i.location LEFT JOIN FETCH i.user WHERE i.isMaster = false AND i.user.id = :userId AND i.course.title = %:filterCourse% AND i.location.id = :filterLocation AND i.validated = :filterValidated AND i.type = :filterType")
		List<Intervention> searchBy(@Param("userId") long userId, @Param("filterCourse") String filterCourse,
				@Param("filterLocation") long filterLocation, @Param("filterValidated") boolean filterValidated, @Param("filterType") InterventionStatus filterType);
		*/
	
		List<Intervention> searchBy(long userId, Map<String, String[]> paramsMap);

}
