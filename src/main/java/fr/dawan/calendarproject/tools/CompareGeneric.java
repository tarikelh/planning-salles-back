package fr.dawan.calendarproject.tools;

import java.lang.reflect.Field;

public class CompareGeneric {
	
	public static <T> String compareObjects(T objAfter, T objBefore) throws Exception {
		Field[] fieldsAfter = objAfter.getClass().getDeclaredFields();
		Field[] fieldsBefore = objBefore.getClass().getDeclaredFields();
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < fieldsAfter.length; i++) {
			fieldsAfter[i].setAccessible(true);
			fieldsBefore[i].setAccessible(true);
			
			if (fieldsAfter[i].get(objAfter) != null && fieldsBefore[i].get(objBefore) != null) {
				if(!fieldsAfter[i].get(objAfter).equals(fieldsBefore[i].get(objBefore))) {			
					builder.append(fieldsAfter[i].getName());
					builder.append(" / ");
				}
			}
		}
		
		return builder.toString();
	}

}
