package fr.dawan.calendarproject.dto;

public class ResourceDto {

	private long Id ;
	
	private int version ;
	
	private int quantity;
	
	private String name;
	
	private long roomId;
	
	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ResourceDto(long id, int version, int quantity, String name, long roomId) {
		super();
		Id = id;
		this.version = version;
		this.quantity = quantity;
		this.name = name;
		this.roomId = roomId;
	}

	public ResourceDto() {
		super();
	}
	
}

