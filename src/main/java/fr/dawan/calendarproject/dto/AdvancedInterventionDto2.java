package fr.dawan.calendarproject.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AdvancedInterventionDto2 extends AdvancedInterventionDto {

	private List<AdvancedInterventionDto> eventSiblings;

	public AdvancedInterventionDto2() {
		super();
		this.eventSiblings = new ArrayList<>();
	}

	public AdvancedInterventionDto2(long id, long idDg2, String slug, String comment, LocationDto location,
			CourseDto course, UserDto user, int attendeesCount, String type, boolean validated, LocalDate dateStart,
			LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd, InterventionDto masterIntervention,
			boolean isMaster, String customers, int version, List<AdvancedInterventionDto> eventSiblings) {
		super(id, idDg2, slug, comment, location, course, user, attendeesCount, type, validated, dateStart, dateEnd,
				timeStart, timeEnd, masterIntervention, isMaster, customers, version);
		this.eventSiblings = eventSiblings;
	}

	public List<AdvancedInterventionDto> getEventSiblings() {
		return eventSiblings;
	}

	public void setEventSiblings(List<AdvancedInterventionDto> eventSiblings) {
		this.eventSiblings = eventSiblings;
	}

}
