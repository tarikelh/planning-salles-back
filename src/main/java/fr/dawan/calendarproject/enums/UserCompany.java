package fr.dawan.calendarproject.enums;

public enum UserCompany {
	DAWAN, JEHANN;
	
	public static boolean contains(String value) {
		for (UserCompany company : UserCompany.values()) {
			if (company.toString().equals(value))
				return true;
		}
		return false;
	}
}
