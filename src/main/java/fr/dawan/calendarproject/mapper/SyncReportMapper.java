package fr.dawan.calendarproject.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import fr.dawan.calendarproject.dto.SyncReportDto;
import fr.dawan.calendarproject.entities.SyncReport;

@Mapper(componentModel = "spring")
public interface SyncReportMapper {
	SyncReportDto syncReportToSyncReportDto(SyncReport syncReport);

	SyncReport syncReportDtoToSyncReport(SyncReportDto syncReportDto);
	
	List<SyncReportDto> listSyncReportToListSyncReportDto(List<SyncReport> syncReports);
}
