package fr.dawan.calendarproject.dto;

public class CenterDto {

	private long id;
	
	private int fullCapacity;
	
	private int reducedCapacity;
	
	private String adress;
	
	private String phoneNumber;
	
	private int version;
	
	private long locationId;

	public CenterDto() {
		super();
	}

	public CenterDto(long id, int fullCapacity, int reducedCapacity, String adress, String phoneNumber, int version,
			long locationId) {
		super();
		this.id = id;
		this.fullCapacity = fullCapacity;
		this.reducedCapacity = reducedCapacity;
		this.adress = adress;
		this.phoneNumber = phoneNumber;
		this.version = version;
		this.locationId = locationId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getFullCapacity() {
		return fullCapacity;
	}

	public void setFullCapacity(int fullCapacity) {
		this.fullCapacity = fullCapacity;
	}

	public int getReducedCapacity() {
		return reducedCapacity;
	}

	public void setReducedCapacity(int reducedCapacity) {
		this.reducedCapacity = reducedCapacity;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getLocationId() {
		return locationId;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}
	
	
	
}
