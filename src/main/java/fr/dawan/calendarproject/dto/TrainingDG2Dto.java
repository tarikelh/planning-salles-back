package fr.dawan.calendarproject.dto;

public class TrainingDG2Dto {
	long trId;
	String title;
	double duration;
	String trainingSlug;
	String registrationSlug;
	String beginAt;
	String finishAt;
	
	public TrainingDG2Dto() {}
	
	public TrainingDG2Dto(long trId, String title, double duration, String trainingSlug, String registrationSlug,
			String beginAt, String finishAt) {
		super();
		this.trId = trId;
		this.title = title;
		this.duration = duration;
		this.trainingSlug = trainingSlug;
		this.registrationSlug = registrationSlug;
		this.beginAt = beginAt;
		this.finishAt = finishAt;
	}
	
	public long getTrId() {
		return trId;
	}
	
	public void setTrId(long trId) {
		this.trId = trId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public double getDuration() {
		return duration;
	}
	
	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	public String getTrainingSlug() {
		return trainingSlug;
	}
	
	public void setTrainingSlug(String trainingSlug) {
		this.trainingSlug = trainingSlug;
	}
	
	public String getRegistrationSlug() {
		return registrationSlug;
	}
	
	public void setRegistrationSlug(String registrationSlug) {
		this.registrationSlug = registrationSlug;
	}
	
	public String getBeginAt() {
		return beginAt;
	}
	
	public void setBeginAt(String beginAt) {
		this.beginAt = beginAt;
	}
	
	public String getFinishAt() {
		return finishAt;
	}
	
	public void setFinishAt(String finishAt) {
		this.finishAt = finishAt;
	}
}
