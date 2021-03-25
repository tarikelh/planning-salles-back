package fr.dawan.calendarproject.entities;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.dawan.calendarproject.dto.InterventionMementoDto;

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
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreatedState;

	//Do we really need version here?
	@Version
	private int version;

	public InterventionMemento() {
		this.dateCreatedState = new Date();
	}

	// rôle sauvegarde de l'état
	// vérifier pour la date
	public InterventionMemento(InterventionMementoDto state) {
		this.state = state;
		this.dateCreatedState = new Date();
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

	public Date getDateCreatedState() {
		return dateCreatedState;
	}

	public void setDateCreatedState(Date dateCreatedState) {
		this.dateCreatedState = dateCreatedState;
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
		builder.append(", state=");
		builder.append(state);
		builder.append(", dateCreatedState=");
		builder.append(dateCreatedState);
		builder.append(", version=");
		builder.append(version);
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
