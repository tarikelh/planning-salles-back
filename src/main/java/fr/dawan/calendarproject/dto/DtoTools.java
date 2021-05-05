package fr.dawan.calendarproject.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;

public class DtoTools {

	public static <TSource, TDestination> TDestination convert(TSource obj, Class<TDestination> clazz) {
		ModelMapper myMapper = new ModelMapper();

		myMapper.typeMap(Intervention.class, InterventionDto.class).addMappings(mapper -> {
			mapper.map(src -> src.getUser().getId(), InterventionDto::setUserId);
			mapper.map(src -> src.getLocation().getId(), InterventionDto::setLocationId);
			mapper.map(src -> src.getCourse().getId(), InterventionDto::setCourseId);
			mapper.map(src -> src.getCourse().getId(), InterventionDto::setCourseId);
			mapper.map(src -> src.getNextIntervention().getId(), InterventionDto::setNextInterventionId);
		});
		
		Converter<Set<Skill>, List<Long>> skillsToIdsList = new AbstractConverter<Set<Skill>, List<Long>>() {
			@Override
			protected List<Long> convert(Set<Skill> context) {
				List<Long> ids = new ArrayList<Long>();
				if (context.isEmpty())
					return null;
				else {
					context.forEach(s -> ids.add(s.getId()));
					return ids;
				}
			}
		};

		myMapper.typeMap(User.class, AdvancedUserDto.class).addMappings(mapper -> {
			mapper.using(skillsToIdsList).map(User::getSkills, AdvancedUserDto::setSkillsId);
			mapper.map(src -> src.getLocation().getId(), AdvancedUserDto::setLocationId);
		});


		Converter<Set<User>, List<Long>> usersToIdsList = new AbstractConverter<Set<User>, List<Long>>() {
			@Override
			protected List<Long> convert(Set<User> context) {
				List<Long> ids = new ArrayList<Long>();
				if (context.isEmpty())
					return null;
				else {
					context.forEach(u -> ids.add(u.getId()));
					return ids;
				}
			}
		};
		
		myMapper.typeMap(Skill.class, AdvancedSkillDto.class).addMappings(mapper -> {
			mapper.using(usersToIdsList).map(Skill::getUsers, AdvancedSkillDto::setUsersId);
		});
		
		return myMapper.map(obj, clazz);
	}
}