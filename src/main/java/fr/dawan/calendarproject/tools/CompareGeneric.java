package fr.dawan.calendarproject.tools;

import java.lang.reflect.Field;

public class CompareGeneric {
	
	/**
	 * Generic method to compare two objects to know which modifications were done. 
	 * <p>
	 * This method is used for interventionMemento to know which modifications are applied to an intervention.
	 * </p>
	 * 
	 * @param objAfter updated object
	 * @param objBefore same object but with the state before the modification
	 * @return String return a String with all fields that were modified between objBefore and objAfter. For instance "dateStart / dateEnd / location /"
	 * 			Return 'null' if the two objects are from different Class
	 * @throws Exception handle exception 'IllegalAccessException' when cannot get value of fields
	 */
	public static <T> String compareObjects(T objAfter, T objBefore) throws Exception {
		if(objAfter.getClass() == objBefore.getClass()) {
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
		} else {
			return null;
		}
		
	}

}
