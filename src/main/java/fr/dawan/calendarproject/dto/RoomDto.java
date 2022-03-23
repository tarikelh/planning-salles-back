
package fr.dawan.calendarproject.dto;

public class RoomDto {
	
	private long id;

	private long idDg2;
	
	private String name;
	
	private long fullCapacity;
	
	private boolean IsAvailable;
	
	private long locationId;
	
	private int version;

	public RoomDto() {
		super();
	}

	public RoomDto(long id, long idDg2, String name, long fullCapacity , boolean isAvailable, long locationId, int version) {
		this.id = id;
		this.idDg2 = idDg2;
		this.name = name;
		this.fullCapacity = fullCapacity;
		IsAvailable = isAvailable;
		this.locationId = locationId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getFullCapacity() {
		return fullCapacity;
	}

	public void setFullCapacity(long fullCapacity) {
		this.fullCapacity = fullCapacity;
	}

	public long getPartialCapacity() {
		return fullCapacity / 2;
	}

	public boolean isAvailable() {
		return IsAvailable;
	}

	public void setAvailable(boolean available) {
		IsAvailable = available;
	}

	public void setIsAvailable(boolean isAvailable) {
		IsAvailable = isAvailable;
	}

	public long getLocationId() {
		return locationId;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}


	
	

}