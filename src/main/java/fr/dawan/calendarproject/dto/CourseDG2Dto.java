package fr.dawan.calendarproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseDG2Dto {

	private String title;
	private String duration;
	private String description;
	private String slug;
	private String alias;
	private String fullAlias;
	private double standardPrice;
	private double customPrice;
	private double customPriceExtra;
	private double remotelyPrice;
	private String objectives;
	private String prerequisites;

	public CourseDG2Dto() {

	}

	public CourseDG2Dto(String title, String duration, String description, String slug, String alias, String fullAlias,
			double standardPrice, double customPrice, double customPriceExtra, double remotelyPrice, String objectives,
			String prerequisites) {
		this.title = title;
		this.duration = duration;
		this.description = description;
		this.slug = slug;
		this.alias = alias;
		this.fullAlias = fullAlias;
		this.standardPrice = standardPrice;
		this.customPrice = customPrice;
		this.customPriceExtra = customPriceExtra;
		this.remotelyPrice = remotelyPrice;
		this.objectives = objectives;
		this.prerequisites = prerequisites;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getFullAlias() {
		return fullAlias;
	}

	public void setFullAlias(String fullAlias) {
		this.fullAlias = fullAlias;
	}

	public double getStandardPrice() {
		return standardPrice;
	}

	public void setStandardPrice(double standardPrice) {
		this.standardPrice = standardPrice;
	}

	public double getCustomPrice() {
		return customPrice;
	}

	public void setCustomPrice(double customPrice) {
		this.customPrice = customPrice;
	}

	public double getCustomPriceExtra() {
		return customPriceExtra;
	}

	public void setCustomPriceExtra(double customPriceExtra) {
		this.customPriceExtra = customPriceExtra;
	}

	public double getRemotelyPrice() {
		return remotelyPrice;
	}

	public void setRemotelyPrice(double remotelyPrice) {
		this.remotelyPrice = remotelyPrice;
	}

	public String getObjectives() {
		return objectives;
	}

	public void setObjectives(String objectives) {
		this.objectives = objectives;
	}

	public String getPrerequisites() {
		return prerequisites;
	}

	public void setPrerequisites(String prerequisites) {
		this.prerequisites = prerequisites;
	}

}
