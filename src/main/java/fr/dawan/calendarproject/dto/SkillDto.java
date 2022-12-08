package fr.dawan.calendarproject.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SkillDto {
	private long id;

	private String title;
	
	private List<Long> usersId;

	private int version;
	
	

	public SkillDto() {
		super();
		this.usersId = new ArrayList<>();
	}

	public SkillDto(long id, String title, List<Long> usersId, int version) {
		super();
		this.id = id;
		this.title = title;
		this.usersId = usersId;
		this.version = version;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<Long> getUsersId() {
		return usersId;
	}

	public void setUsersId(List<Long> usersId) {
		this.usersId = usersId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, usersId, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkillDto other = (SkillDto) obj;
		return id == other.id && Objects.equals(title, other.title) && Objects.equals(usersId, other.usersId)
				&& version == other.version;
	}
	
	
	
}
