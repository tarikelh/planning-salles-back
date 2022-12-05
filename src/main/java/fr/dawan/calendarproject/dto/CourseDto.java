package fr.dawan.calendarproject.dto;

public class CourseDto {

	private long id;
	
	private long idDg2;

	private String title;

	private double duration;

	private String slug;

	private int version;

	public CourseDto() {
	}

	public CourseDto(long id, long idDg2, String title, double duration, String slug, int version) {
		this.id = id;
		this.idDg2 = idDg2;
		this.title = title;
		this.duration = duration;
		this.slug = slug;
		this.version = version;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getIdDg2() {
		return idDg2;
	}

	public void setIdDg2(long idDg2) {
		this.idDg2 = idDg2;
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

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
