package fr.dawan.calendarproject.dto;

import javax.persistence.Embeddable;

@Embeddable
public class MementoMessageDto {
	
	private long messageInterventionId;
	
	private String messageAction;
	
	private String userEmail;
	
	private String modificationsDone;
	
	public MementoMessageDto() {
	}
		
	public MementoMessageDto(long messageInterventionId, String messageAction, String userEmail, String modificationsDone) {
		this.messageInterventionId = messageInterventionId;
		this.messageAction = messageAction;
		this.userEmail = userEmail;
		this.modificationsDone = modificationsDone;
	}

	public long getMessageInterventionId() {
		return messageInterventionId;
	}

	public void setMessageInterventionId(long messageInterventionId) {
		this.messageInterventionId = messageInterventionId;
	}

	public String getMessageAction() {
		return messageAction;
	}

	public void setMessageAction(String messageAction) {
		this.messageAction = messageAction;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getModificationsDone() {
		return modificationsDone;
	}

	public void setModificationsDone(String modificationsDone) {
		this.modificationsDone = modificationsDone;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(messageInterventionId);
		builder.append(";");
		builder.append(messageAction);
		builder.append(";");
		builder.append(userEmail);
		builder.append(";");
		builder.append(modificationsDone);
		return builder.toString();
	}
}
