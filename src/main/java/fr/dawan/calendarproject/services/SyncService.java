package fr.dawan.calendarproject.services;

import java.util.List;

import fr.dawan.calendarproject.dto.SyncReportDto;
import fr.dawan.calendarproject.entities.SyncReport;

public interface SyncService {
	String allDG2Import() throws Exception;
	
	String saveSyncReport(SyncReport syncReport);
	
	List<SyncReportDto> getAll();
	
	SyncReportDto getLast();
}
