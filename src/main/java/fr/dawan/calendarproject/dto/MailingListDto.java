package fr.dawan.calendarproject.dto;

import java.util.List;

public class MailingListDto {
	
	private List<Long> usersId;
	private String dateStart;
	private String dateEnd;
	
	public MailingListDto() {
	}

	public MailingListDto(List<Long> usersId, String dateStart, String dateEnd) {
		this.usersId = usersId;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}

	public List<Long> getUsersId() {
		return usersId;
	}

	public void setUsersId(List<Long> usersId) {
		this.usersId = usersId;
	}

	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	
}
