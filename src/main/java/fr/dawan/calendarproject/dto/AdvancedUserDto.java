package fr.dawan.calendarproject.dto;

import java.util.List;

public class AdvancedUserDto extends UserDto {

	private List<Long> skillsId;

	public List<Long> getSkillsId() {
		return skillsId;
	}

	public void setSkillsId(List<Long> skillsId) {
		this.skillsId = skillsId;
	}
}