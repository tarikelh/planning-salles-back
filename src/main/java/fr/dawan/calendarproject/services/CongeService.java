package fr.dawan.calendarproject.services;

import fr.dawan.calendarproject.dto.CongeDto;

import java.util.List;

public interface CongeService {

	List<CongeDto> getAllConges();

	List<CongeDto> getAllCongesByEmployeeId(long id);

	void fetchAllDG2Conges(String email, String password) throws Exception;
}
