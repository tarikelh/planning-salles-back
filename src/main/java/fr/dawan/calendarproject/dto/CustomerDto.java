package fr.dawan.calendarproject.dto;

public class CustomerDto {
	private String companyName;
	private int nb;
	
	public CustomerDto() {
	}

	public CustomerDto(String companyName, int nb) {
		this.companyName = companyName;
		this.nb = nb;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public int getNb() {
		return nb;
	}
	
	public void setNb(int nb) {
		this.nb = nb;
	}
	
	
}
