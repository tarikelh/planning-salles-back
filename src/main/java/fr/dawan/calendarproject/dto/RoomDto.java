
package fr.dawan.calendarproject.dto;

@SuppressWarnings("serial")
public class RoomDto {
	
	private long id;
	
	private String name;
	
	private long fullCapacity;
	
	private long partialCapacity;
	
	private boolean IsAvailable;
	
	private long locationId;
	
	private int version;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
		return partialCapacity;
	}

	public void setPartialCapacity() {
		this.partialCapacity = this.fullCapacity/2;
		//to ask 
	}

	public boolean isIsAvailable() {
		return IsAvailable;
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

	public RoomDto(long id, String name, long fullCapacity, long partialCapacity, boolean isAvailable, long locationId,
			int version) {
		super();
		this.id = id;
		this.name = name;
		this.fullCapacity = fullCapacity;
		this.partialCapacity = partialCapacity;
		IsAvailable = isAvailable;
		this.locationId = locationId;
		this.version = version;
	}

	public RoomDto() {
		super();
	}
	
	

}