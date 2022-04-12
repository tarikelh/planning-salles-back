package fr.dawan.calendarproject.services;

import fr.dawan.calendarproject.dto.LeavePeriodDto;

import java.util.List;

public interface LeavePeriodService {

	List<LeavePeriodDto> getAllLeavePeriods();

	List<LeavePeriodDto> getAllLeavePeriodsByEmployeeId(long id);

	void fetchAllDG2LeavePeriods(String email, String password, String firstDay, String lastDay) throws Exception;
}
