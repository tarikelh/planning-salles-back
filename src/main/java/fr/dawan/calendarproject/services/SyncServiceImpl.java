package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.LoginDto;
import fr.dawan.calendarproject.dto.SyncReportDto;
import fr.dawan.calendarproject.entities.SyncReport;
import fr.dawan.calendarproject.mapper.SyncReportMapper;
import fr.dawan.calendarproject.repositories.SyncRepository;

@Service
public class SyncServiceImpl implements SyncService {

	//TODO add comments

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

	@Autowired
	SyncRepository syncRepository;

	@Autowired
	SyncReportMapper syncReportMapper;

	@Value("${sync.service.minusMonth}")
	private int minusMonth;

	@Value("${sync.service.plusMonth}")
	private int plusMonth;

//	@Value("${sync.service.email}")
//	private String email;
//
//	@Value("${sync.service.password}")
//	private String password;

	@Override
	public String allDG2Import(LoginDto loginDto) throws Exception {
		long startMilli = System.currentTimeMillis();

		StringBuffer result = new StringBuffer();
		List<String> errors = new ArrayList<>();
		SyncReport syncReport = new SyncReport();

		LocalDate dateNow = LocalDate.now();
		LocalDate dateStart = dateNow.minusMonths(minusMonth).minusDays(dateNow.getDayOfMonth() - 1);
		LocalDate dateEnd = dateNow.plusMonths(plusMonth).plusDays(
				dateNow.plusMonths(plusMonth).lengthOfMonth() - dateNow.plusMonths(plusMonth).getDayOfMonth());

		syncReport.setEnd(dateEnd);
		syncReport.setStart(dateStart);
		syncReport.setStartOfSync(LocalDateTime.now());

		// try courses import
		try {
			courseService.fetchAllDG2Courses(loginDto.getEmail(), loginDto.getPassword());
		} catch (Exception e) {
			errors.add("Courses import fail !");
		}

		// try locations import
		try {
			locationService.fetchAllDG2Locations(loginDto.getEmail(), loginDto.getPassword());
		} catch (Exception e) {
			errors.add("Locations import fail !");
		}

		// try users and skills import
		try {
			userService.fetchAllDG2Users(loginDto.getEmail(), loginDto.getPassword());
		} catch (Exception e) {
			errors.add("Users and Skills import fail !");
		}

		// try intervention / leave-period import
		try {
			interventionService.fetchDG2Interventions(loginDto.getEmail(), loginDto.getPassword(), dateStart, dateEnd);
		} catch (Exception e) {
			errors.add("Interventions and Leave-periods import fail !");
		}

		// try task import
		try {
			taskService.fetchAllDG2Task(loginDto.getEmail(), loginDto.getPassword(), dateStart, dateEnd);
		} catch (Exception e) {
			errors.add("Task import fail !");
		}

		// try intervention followed import
		try {
			followedService.fetchAllDG2InterventionsFollowed(loginDto.getEmail(), loginDto.getPassword(), dateStart, dateEnd);
		} catch (Exception e) {
			errors.add("InterventionsFollowed import fail !");
		}

		// end imports
		syncReport.setDurationInMilli( System.currentTimeMillis() - startMilli);
		syncReport.setEndOfSync(LocalDateTime.now());


		if (errors.isEmpty()) {
			result.append("Sync With DG2 pass between : ")
			      .append(dateStart.toString())
			      .append(" and ")
				  .append(dateEnd.toString());
		} else {
			syncReport.setFailed(true);
			for (String error : errors) {
				result.append(error);
				result.append(" \n");
			}
			syncReport.setMessage(result.toString());
			throw new Exception(saveSyncReport(syncReport));
		}
		syncReport.setMessage(result.toString());
		return saveSyncReport(syncReport);
	}

	@Override
	public String locationsDG2Import(LoginDto loginDto) throws Exception {
		long startMilli = System.currentTimeMillis();

		StringBuffer result = new StringBuffer();
		List<String> errors = new ArrayList<>();
		SyncReport syncReport = new SyncReport();

		LocalDate dateNow = LocalDate.now();
		LocalDate dateStart = dateNow.minusMonths(minusMonth).minusDays(dateNow.getDayOfMonth() - 1);
		LocalDate dateEnd = dateNow.plusMonths(plusMonth).plusDays(
				dateNow.plusMonths(plusMonth).lengthOfMonth() - dateNow.plusMonths(plusMonth).getDayOfMonth());

		syncReport.setEnd(dateEnd);
		syncReport.setStart(dateStart);
		syncReport.setStartOfSync(LocalDateTime.now());

		// try locations import
		try {
			locationService.fetchAllDG2Locations(loginDto.getEmail(), loginDto.getPassword());
		} catch (Exception e) {
			errors.add("Locations import fail !");
		}

		// end imports
		syncReport.setDurationInMilli( System.currentTimeMillis() - startMilli);
		syncReport.setEndOfSync(LocalDateTime.now());


		if (errors.isEmpty()) {
			result.append("Sync With DG2 pass between : ")
					.append(dateStart.toString())
					.append(" and ")
					.append(dateEnd.toString());
		} else {
			syncReport.setFailed(true);
			for (String error : errors) {
				result.append(error);
				result.append(" \n");
			}
			syncReport.setMessage(result.toString());
			throw new Exception(saveSyncReport(syncReport));
		}
		syncReport.setMessage(result.toString());
		return saveSyncReport(syncReport);
	}

	@Override
	public String saveSyncReport(SyncReport syncReport) {
		return syncRepository.saveAndFlush(syncReport).toString();
	}

	@Override
	public List<SyncReportDto> getAll() {
		return syncReportMapper.listSyncReportToListSyncReportDto(syncRepository.findAll());
	}

	@Override
	public SyncReportDto getLast() {
		return syncReportMapper.syncReportToSyncReportDto(syncRepository.findLastByOrderByEndOfSync().orElse(null));
	}
}
