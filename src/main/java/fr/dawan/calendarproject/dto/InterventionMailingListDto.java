package fr.dawan.calendarproject.dto;

import java.util.List;

public class InterventionMailingListDto implements Cloneable {
	
	private String[] emails;
	private List<Long> interventionIds;

	public InterventionMailingListDto(String[] emails, List<Long> interventionIds) {
		this.emails = emails;
		this.interventionIds = interventionIds;
	}

	public String[] getEmails() {
		return emails;
	}
	
	public void setEmails(String[] emails) {
		this.emails = emails;
	}

	public List<Long> getInterventionIds() {
		return interventionIds;
	}

	public void setInterventionIds(List<Long> interventionIds) {
		this.interventionIds = interventionIds;
	}
}

