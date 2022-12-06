package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
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

	@Value("${sync.service.startmonth}")
	private int minusMonth;

	@Value("${sync.service.endmonth}")
	private int plusMonth;

	@Value("${sync.service.email}")
	private String email;

	@Value("${sync.service.password}")
	private String password;

	@Override
	public String allDG2Import() throws Exception {
		StringBuffer result = new StringBuffer();
		List<String> errors = new ArrayList<>();

		LocalDate dateNow = LocalDate.now();
		LocalDate dateStart = dateNow.minusMonths(minusMonth).minusDays(dateNow.getDayOfMonth() - 1);
		LocalDate dateEnd = dateNow.plusMonths(plusMonth).plusDays(
				dateNow.plusMonths(plusMonth).lengthOfMonth() - dateNow.plusMonths(plusMonth).getDayOfMonth());

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
			interventionService.fetchDG2Interventions(email, password, dateStart, dateEnd);
		} catch (Exception e) {
			errors.add("Interventions and Leave-periods import fail !");
		}

		// try task import
		try {
			taskService.fetchAllDG2Task(email, password, dateStart, dateEnd);
		} catch (Exception e) {
			errors.add("Task import fail !");
		}

		// try intervention followed import
		try {
			followedService.fetchAllDG2InterventionsFollowed(email, password, dateStart, dateEnd);
		} catch (Exception e) {
			errors.add("InterventionsFollowed import fail !");
		}

		if (errors.isEmpty()) {
			result.append("Sync With DG2 pass between : ")
			      .append(dateStart.toString())
			      .append(" and ")
				  .append(dateEnd.toString());
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
