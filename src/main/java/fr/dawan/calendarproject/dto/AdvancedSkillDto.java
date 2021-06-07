package fr.dawan.calendarproject.dto;

import java.util.List;

public class AdvancedSkillDto extends SkillDto {
	
	private List<Long> usersId;
	
	public List<Long> getUsersId() {
		return usersId;
	}

	public void setUsersId(List<Long> usersId) {
		this.usersId = usersId;
	}

}