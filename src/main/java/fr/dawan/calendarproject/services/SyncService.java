package fr.dawan.calendarproject.services;

import java.time.LocalDate;

public interface SyncService {
	String allDG2Import(String email, String password, LocalDate start, LocalDate end) throws Exception;
}
