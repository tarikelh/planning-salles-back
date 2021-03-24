package fr.dawan.calendarproject.entities;

import java.util.Date;

import javax.persistence.Column;
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

	@Temporal(TemporalType.DATE)
	@Column(nullable = true)
	private Date dateState;

	@Version
	private int version;

	public InterventionMemento() {
		this.dateState = new Date();
	}

	// rôle sauvegarde de l'état
	// vérifier pour la date
	public InterventionMemento(InterventionMementoDto state) {
		this.state = state;
		this.dateState = new Date();
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

	public Date getDateState() {
		return dateState;
	}

	public void setDateState(Date dateState) {
		this.dateState = dateState;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InterventionMemento [state=").append(state).append("]");
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
