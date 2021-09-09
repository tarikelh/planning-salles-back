package fr.dawan.calendarproject.entities;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.dto.MementoMessageDto;

//Memento Class  >>  Previously OperationModif
@Entity
public class InterventionMemento implements Cloneable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Embedded
	private InterventionMementoDto state;
	
	private LocalDateTime dateCreatedState;
	
	@Embedded
	private MementoMessageDto mementoMessage;

	@Version
	private int version;

	public InterventionMemento() {
		this.dateCreatedState = LocalDateTime.now();
	}

	public InterventionMemento(InterventionMementoDto state) {
		this.state = state;
		this.dateCreatedState = LocalDateTime.now();
	}
	

	public InterventionMementoDto getState() {
		return state;
	}

	public void setState(InterventionMementoDto state) {
		this.state = state;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getDateCreatedState() {
		return dateCreatedState;
	}

	public void setDateCreatedState(LocalDateTime dateCreatedState) {
		this.dateCreatedState = dateCreatedState;
	}
	
	public MementoMessageDto getMementoMessage() {
		return mementoMessage;
	}

	public void setMementoMessage(MementoMessageDto mementoMessage) {
		this.mementoMessage = mementoMessage;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	//Change all toString for a StringBuilder and not a concatenation
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(id);
		builder.append(";");
		builder.append(state);
		builder.append(";");
		builder.append(dateCreatedState);
		builder.append(";");
		builder.append(version);
		builder.append(";");
		builder.append(mementoMessage);
		return builder.toString();
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
