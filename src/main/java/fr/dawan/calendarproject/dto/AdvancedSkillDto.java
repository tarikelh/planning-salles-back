package fr.dawan.calendarproject.dto;

import java.util.ArrayList;
import java.util.List;

public class AdvancedSkillDto extends SkillDto {
	
	private List<Long> usersId;
	
	public AdvancedSkillDto() {
		super();
		this.usersId = new ArrayList<Long>();
	}

	public AdvancedSkillDto(long id, String title, int version, List<Long> usersId) {
		super(id, title, version);
		this.usersId = usersId;
	}

	public List<Long> getUsersId() {
		return usersId;
	}

	public void setUsersId(List<Long> usersId) {
		this.usersId = usersId;
	}
}