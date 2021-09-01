package fr.dawan.calendarproject.entities;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import org.springframework.beans.factory.annotation.Autowired;

import fr.dawan.calendarproject.dto.InterventionMementoDto;
import fr.dawan.calendarproject.dto.MementoMessageDto;
import fr.dawan.calendarproject.tools.JwtTokenUtil;

//Memento Class  >>  Previously OperationModif
@Entity
public class InterventionMemento implements Cloneable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Embedded
	private InterventionMementoDto state;

	//Verify with the group to set up the time >> change for TIMESTAMP in order to have date + time because can update the same day so we need to have the time
	//Question : do they prefer to seperate the date and the time?
	//Need to fix the time for FR
	/*
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreatedState;
	*/
	
	private LocalDateTime dateCreatedState;
	
	@Embedded
	private MementoMessageDto mementoMessage;

	//Do we really need version here?
	@Version
	private int version;

	public InterventionMemento() {
		this.dateCreatedState = LocalDateTime.now();
	}

	// rôle sauvegarde de l'état
	// vérifier pour la date
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
		builder.append("InterventionMemento [id=");
		builder.append(id);
		builder.append(";");
		builder.append("state=");
		builder.append(state);
		builder.append(";");
		builder.append("dateCreatedState=");
		builder.append(dateCreatedState);
		builder.append(";");
		builder.append("version=");
		builder.append(version);
		builder.append(";");
		builder.append("mementoMessage=");
		builder.append(mementoMessage);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	//Memento Methods
	public InterventionMemento createMemento() throws Exception {
		//InterventionMemento temp = new InterventionMemento((InterventionMementoDto)this.clone());
		InterventionMemento temp = new InterventionMemento();
		temp = (InterventionMemento) this.clone();
		return temp;
	}
	
	public void restore(InterventionMemento memento) throws Exception {	
		//InterventionMementoDto myIntervention = memento.getState();
		this.state = memento.getState();
	} 
}
