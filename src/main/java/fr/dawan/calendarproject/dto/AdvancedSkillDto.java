package fr.dawan.calendarproject.dto;

import java.util.ArrayList;
import java.util.List;

public class AdvancedSkillDto extends SkillDto {

	private List<Long> usersId;

	public AdvancedSkillDto() {
		this.usersId = new ArrayList<>();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((usersId == null) ? 0 : usersId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdvancedSkillDto other = (AdvancedSkillDto) obj;
		if (usersId == null) {
			if (other.usersId != null)
				return false;
		} else if (!usersId.equals(other.usersId))
			return false;
		return true;
	}
}