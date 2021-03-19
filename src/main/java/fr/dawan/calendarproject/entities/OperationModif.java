package fr.dawan.calendarproject.entities;

//Memento Class
public class OperationModif {
	
	//InterventionMemento
	//id, ancienneValeur, nouvValeur (voir Design Pattern "Memento")
	
	//@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	//private long id;
	
	private Intervention state;

	public OperationModif(Intervention state) {
		this.state = state;
	}

	public Intervention getState() {
		return state;
	}

	public void setState(Intervention state) {
		this.state = state;
	}
	
}
