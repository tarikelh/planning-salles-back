package fr.dawan.calendarproject.dto;

import java.util.Set;

import javax.persistence.Embeddable;


public class AvancedUserDto extends UserDto {

	private Set<SkillDto> skills;

	public Set<SkillDto> getSkills() {
		return skills;
	}

	public void setSkills(Set<SkillDto> skills) {
		this.skills = skills;
	}

}
