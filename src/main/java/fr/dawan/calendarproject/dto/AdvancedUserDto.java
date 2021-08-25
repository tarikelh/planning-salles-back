package fr.dawan.calendarproject.dto;

import java.util.ArrayList;
import java.util.List;

public class AdvancedUserDto extends UserDto {

	private List<Long> skillsId;

	public AdvancedUserDto() {
		this.skillsId = new ArrayList<Long>();
	}
	
	public AdvancedUserDto(long id, String firstName, String lastName, long locationId, String email, String password,
			String type, String company, String imagePath, int version, List<Long> skillsId) {
		super(id, firstName, lastName, locationId, email, password,
				 type, company, imagePath, version);
		this.skillsId = skillsId;
	}

	public List<Long> getSkillsId() {
		return skillsId;
	}

	public void setSkillsId(List<Long> skillsId) {
		this.skillsId = skillsId;
	}
}