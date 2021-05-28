package fr.dawan.calendarproject.enums;

import java.util.HashSet;
import java.util.Set;

public enum InterventionStatus {
	INTERN, SUR_MESURE;

	private static Set<String> _values = new HashSet<>();

	static {
		for (InterventionStatus status : InterventionStatus.values()) {
			_values.add(status.name());
		}
	}

	public static boolean contains(String value) {
		return _values.contains(value);
	}
}
