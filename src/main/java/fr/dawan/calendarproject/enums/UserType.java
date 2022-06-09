package fr.dawan.calendarproject.enums;

public enum UserType {
	 ADMINISTRATIF, FORMATEUR, COMMERCIAL, INDEPENDANT, APPRENTI, IT,CONTA, RH, UI, Manager,NOT_FOUND,  INTERV_NOT_ASSIGN;

	public static boolean contains(String value) {
		for (UserType type : UserType.values()) {
			if (type.toString().equals(value))
				return true;
		}
		return false;
	}
}
