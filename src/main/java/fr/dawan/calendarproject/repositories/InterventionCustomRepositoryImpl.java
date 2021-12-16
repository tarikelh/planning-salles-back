package fr.dawan.calendarproject.repositories;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.enums.InterventionStatus;

@Repository
public class InterventionCustomRepositoryImpl implements InterventionCustomRepository {

	@PersistenceContext
	private EntityManager em; // objet connexion

	public static boolean checkEmptyArray(String[] strArray) {
		boolean isEmpty = true;
		for (String s : strArray) {
			if (!s.trim().equals("")) {
				isEmpty = false;
				break;
			}
		}
		return isEmpty;
	}

	// URL :
	// /api/interventions/filter/{userId}?filterCourse=agile&filterLocation=1&filterValidated=true&filterType=INTERN
	// paramsMap :
	// ?filterCourse=agile&filterLocation=1&filterValidated=true&filterType=INTERN
	private Query getQueryBy(long userId, String req, Map<String, String[]> paramsMap) {
		StringBuilder jpqlRequest = new StringBuilder(req);
		
		for (String param : paramsMap.keySet()) {
			if (!checkEmptyArray(paramsMap.get(param))) {
				if (param.equals("filterCourse")) {
					jpqlRequest.append(" AND i.course.title like :" + param);
				} else if (param.equals("filterLocation")) {
					jpqlRequest.append(" AND i.location.id = :" + param);
				} else if (param.equals("filterValidated")) {
					jpqlRequest.append(" AND i.validated = :" + param);
				} else if (param.equals("filterType")) {
					jpqlRequest.append(" AND i.type = :" + param);
				} else if (param.equals("filterDate")) {
					jpqlRequest.append(" AND  EXTRACT(YEAR FROM i.dateStart) = :" + param);
				}
			}
		}
		
		if (userId != 0) {
			jpqlRequest.append(" AND i.user.id = " + userId);
		}
		
		jpqlRequest.append(" ORDER BY i.dateStart ASC");

		Query query = em.createQuery(jpqlRequest.toString());
		for (String param : paramsMap.keySet()) {
			if (!checkEmptyArray(paramsMap.get(param))) {
				try {
					Class<?> paramType = String.class;
					if (param.equals("filterLocation")) {
						paramType = long.class;
					} else if (param.equals("filterValidated")) {
						paramType = boolean.class;
					} else if (param.equals("filterType")) {
						paramType = InterventionStatus.class;
					} else if (param.equals("filterDate")) {
						paramType = LocalDate.class;
					}

					if (paramType.equals(String.class)) {
						query.setParameter(param, "%" + paramsMap.get(param)[0] + "%");
					} else if (paramType.equals(float.class)) {
						query.setParameter(param, Float.parseFloat(paramsMap.get(param)[0]));
					} else if (paramType.equals(double.class)) {
						query.setParameter(param, Double.parseDouble(paramsMap.get(param)[0]));
					} else if (paramType.equals(boolean.class)) {
						query.setParameter(param, Boolean.parseBoolean(paramsMap.get(param)[0]));
					} else if (paramType.equals(long.class)) {
						query.setParameter(param, Long.parseLong(paramsMap.get(param)[0]));
					} else if (paramType.equals(InterventionStatus.class)) {
						query.setParameter(param, InterventionStatus.valueOf(paramsMap.get(param)[0]));
					} else if (paramType.equals(LocalDate.class)) {
						DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
						query.setParameter(param, LocalDate.parse(paramsMap.get(param)[0], dateFormat).getYear());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return query;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Intervention> searchBy(long userId, Map<String, String[]> paramsMap) {
		String req = "SELECT i FROM Intervention i WHERE i.isMaster = false";
		Query query = getQueryBy(userId, req, paramsMap);
		return (List<Intervention>) query.getResultList();
	}

}
