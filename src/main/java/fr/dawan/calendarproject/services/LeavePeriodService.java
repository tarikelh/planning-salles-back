package fr.dawan.calendarproject.services;

import java.time.LocalDate;
import java.util.List;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.LeavePeriodDto;

public interface LeavePeriodService {

	List<LeavePeriodDto> getAllLeavePeriods();

	List<LeavePeriodDto> getAllLeavePeriodsByEmployeeId(long id);

	int fetchAllDG2LeavePeriods(String email, String password, String firstDay, String lastDay) throws Exception;

	List<LeavePeriodDto> getBetween(String type, LocalDate localDate, LocalDate localDate2);

	CountDto count(String type);
}
