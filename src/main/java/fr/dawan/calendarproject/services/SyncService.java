package fr.dawan.calendarproject.services;

import fr.dawan.calendarproject.entities.SyncReport;

public interface SyncService {
	String allDG2Import() throws Exception;
	
	String saveSyncReport(SyncReport syncReport);
}
