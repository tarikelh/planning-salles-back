package fr.dawan.calendarproject.dto;

public class MessageWebsocketDto {
	
	private String type;
	private String id;
	private String event;


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@Override
	public String toString() {
		return "MessageWebsocketDto [type=" + type + ", id=" + id + ", event=" + event + "]";
	}

}
