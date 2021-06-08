package fr.dawan.calendarproject.enums;

public enum UserType {
	FORMATEUR, COMMERCIAL, ADMINISTRATIF, INDEPENDANT;
	
	public static boolean contains(String value) {
		for (UserType type : UserType.values()) {
			if (type.toString().equals(value))
				return true;
		}
		return false;
	}
}
