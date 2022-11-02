package fr.dawan.calendarproject.enums;

import fr.dawan.calendarproject.entities.User;

public enum InterventionStatus {
	INTERN, INTRA, POE, TP, SOUS_TRAITANT;

	
	public static boolean contains(String value) {
		for (InterventionStatus item : InterventionStatus.values()) {
			if (item.toString().equals(value))
				return true;
		}
		
		return false;
	}
}
