package fr.dawan.calendarproject.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.List;

public class CsvToolsGeneric {

	public static <T> void toCsv(String filePath, List<T> lp, String separator) throws Exception {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
			if (lp != null && lp.size() > 0) {
				Field[] fields = lp.get(0).getClass().getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					fields[i].setAccessible(true);
					// Entity that contains Object
					if (fields[i].getType().getSimpleName().equalsIgnoreCase("int")
							|| fields[i].getType().getSimpleName().equalsIgnoreCase("long")
							|| fields[i].getType().getSimpleName().equalsIgnoreCase("double")
							|| fields[i].getType().getSimpleName().equalsIgnoreCase("String")
							|| fields[i].getType().getSimpleName().equalsIgnoreCase("LocalDate")
							|| fields[i].getType().getSimpleName().equalsIgnoreCase("LocalDateTime")) {
						bw.write(fields[i].getName());
						if (i < fields.length - 1) {
							bw.write(separator);							
						}
					} else {
						Class<?> cls = Class.forName(fields[i].getType().getName());
						Object clsInstance = (Object) cls.newInstance();
						Field[] subFields = clsInstance.getClass().getDeclaredFields();
						for (int j = 0; j < subFields.length; j++) {
							bw.write(subFields[j].getName());
							bw.write(separator);
						}
					}
				}
				bw.newLine();
				for (T obj : lp) {
					for (int i = 0; i < fields.length; i++) {
						fields[i].setAccessible(true);

						if (fields[i].get(obj) != null) {
							bw.write(fields[i].get(obj).toString());
						}
						if (i < fields.length - 1) {
							bw.write(separator);
						}
					}
					bw.newLine();
				}
			}
		}
	}

}
