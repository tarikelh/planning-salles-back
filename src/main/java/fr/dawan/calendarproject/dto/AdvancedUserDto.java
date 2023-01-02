package fr.dawan.calendarproject.dto;

import java.util.ArrayList;
import java.util.List;

public class AdvancedUserDto extends UserDto {

	private List<SkillDto> skills;

	public AdvancedUserDto() {
		this.skills = new ArrayList<>();
	}

	public AdvancedUserDto(long id, long idDg2, long employeeIdDg2, String firstName, String lastName, long locationId, String email,
			String type, String company, String imagePath, String endDate, int version, List<SkillDto> skills) {
		super(id, idDg2, employeeIdDg2, firstName, lastName, locationId, email, type, company, imagePath, endDate, version);
		setSkills(skills);
	}

	public List<SkillDto> getSkills() {
		return skills;
	}

	public void setSkills(List<SkillDto> skills) {
		this.skills = skills;
	}
	
}