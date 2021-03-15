package fr.dawan.calendarproject.entities;

public class OperationModif {
	
	//InterventionMemento
	//id, ancienneValeur, nouvValeur (voir Design Pattern "Memento")
	
	//@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private Intervention oldState;
	
	private Intervention newState;

}
