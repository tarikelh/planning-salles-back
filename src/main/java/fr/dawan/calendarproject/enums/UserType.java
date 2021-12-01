package fr.dawan.calendarproject.enums;

public enum UserType {
	FORMATEUR, COMMERCIAL, ADMINISTRATIF, INDEPENDANT, APPRENTI, NOT_FOUND;

	public static boolean contains(String value) {
		for (UserType type : UserType.values()) {
			if (type.toString().equals(value))
				return true;
		}
		return false;
	}
}
