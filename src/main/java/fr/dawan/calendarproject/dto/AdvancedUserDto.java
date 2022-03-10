package fr.dawan.calendarproject.dto;

import java.util.ArrayList;
import java.util.List;

public class AdvancedUserDto extends UserDto {

	private List<String> skills;

	public AdvancedUserDto() {
		this.skills = new ArrayList<>();
	}

	public AdvancedUserDto(long id, long idDg2, String firstName, String lastName, long locationId, String email, String password,
			String type, String company, String imagePath, int version, List<String> skills) {
		super(id, idDg2, firstName, lastName, locationId, email, password, type, company, imagePath, version);
		setSkills(skills);
	}

	public List<String> getSkills() {
		return skills;
	}

	public void setSkills(List<String> skills) {
		this.skills = skills;
	}
}