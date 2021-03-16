package fr.dawan.calendarproject.dto;

import java.util.Set;

public class AvancedUserDto extends UserDto {

	private Set<SkillDto> skills;

	public Set<SkillDto> getSkills() {
		return skills;
	}

	public void setSkills(Set<SkillDto> skills) {
		this.skills = skills;
	}

}
