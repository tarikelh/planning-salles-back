package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SyncServiceImpl implements SyncService {

	@Autowired
	CourseService courseService;

	@Autowired
	LocationService locationService;

	@Autowired
	UserService userService;

	@Autowired
	InterventionService interventionService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	InterventionFollowedService followedService;

	@Override
	public String allDG2Import(String email, String password, LocalDate start, LocalDate end) throws Exception {
		StringBuffer result = new StringBuffer();
		List<String> errors = new ArrayList<>();

		// try courses import
		try {
			courseService.fetchAllDG2Courses(email, password);
		} catch (Exception e) {
			errors.add("Courses import fail !");
		}

		// try locations import
		try {
			locationService.fetchAllDG2Locations(email, password);
		} catch (Exception e) {
			errors.add("Locations import fail !");
		}

		// try users and skills import
		try {
			userService.fetchAllDG2Users(email, password);
		} catch (Exception e) {
			errors.add("Users and Skills import fail !");
		}

		// try intervention / leave-period import
		try {
			interventionService.fetchDG2Interventions(email, password, start, end);
		} catch (Exception e) {
			errors.add("Interventions and Leave-periods import fail !");
		}
		
		// try task import
		try {
			taskService.fetchAllDG2Task(email, password, start, end);
		} catch (Exception e) {
			errors.add("Interventions and Leave-periods import fail !");
		}
		
		// try intervention followed import
		try {
			followedService.fetchAllDG2InterventionsFollowed(email, password, start, end);
		} catch (Exception e) {
			errors.add("Interventions and Leave-periods import fail !");
		}

		if (errors.isEmpty()) {
			result.append("Sync With DG2 pass");
		} else {
			for (String error : errors) {
				result.append(error);
				result.append(" \n");
			}
			throw new Exception(result.toString());
		}
		return result.toString();
	}
}
