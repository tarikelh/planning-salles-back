package fr.dawan.calendarproject.dto;

import java.util.ArrayList;
import java.util.List;

public class HistoricDto {
	List<CourseDG2Dto> animated;
	List<TrainingDG2Dto> followed;
	
	public HistoricDto() {
		this.animated = new ArrayList<>();
		this.followed = new ArrayList<>();
	}

	public HistoricDto(List<CourseDG2Dto> animated, List<TrainingDG2Dto> followed) {
		this.animated = animated;
		this.followed = followed;
	}

	public List<CourseDG2Dto> getAnimated() {
		return animated;
	}

	public void setAnimated(List<CourseDG2Dto> animated) {
		this.animated = animated;
	}

	public List<TrainingDG2Dto> getFollowed() {
		return followed;
	}

	public void setFollowed(List<TrainingDG2Dto> followed) {
		this.followed = followed;
	}
}
