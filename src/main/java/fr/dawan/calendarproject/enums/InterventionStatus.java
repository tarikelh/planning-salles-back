package fr.dawan.calendarproject.enums;

public enum InterventionStatus {
	INTERN, SUR_MESURE;

	public static boolean contains(String value) {
		for (InterventionStatus item : InterventionStatus.values()) {
			if (item.toString().equals(value))
				return true;
		}
		return false;
	}
}
