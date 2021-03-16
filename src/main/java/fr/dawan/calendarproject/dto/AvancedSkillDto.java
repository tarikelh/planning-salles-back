package fr.dawan.calendarproject.dto;

import java.util.Set;

public class AvancedSkillDto extends SkillDto {
	private Set<UserDto> users;

	public Set<UserDto> getUsers() {
		return users;
	}

	public void setUsers(Set<UserDto> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Skill [id=" + getId() + ", title=" + getTitle() + ", Users=" + getUsers() + "]";
	}

}
