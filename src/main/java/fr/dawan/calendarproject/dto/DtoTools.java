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
		
		myMapper.typeMap(AdvancedUserDto.class, User.class).addMappings(mapper -> {
			mapper.map(AdvancedUserDto::getCompany, User::setEnumCompany);
			mapper.map(AdvancedUserDto::getType, User::setEnumType);
		});

		myMapper.typeMap(User.class, AdvancedUserDto.class).addMappings(mapper -> {
			mapper.using(skillsToIdsList).map(User::getSkills, AdvancedUserDto::setSkillsId);
			mapper.map(User::getEnumCompany, AdvancedUserDto::setCompany);
			mapper.map(User::getEnumType, AdvancedUserDto::setType);
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

		myMapper.typeMap(InterventionDto.class, Intervention.class).addMappings(mapper -> {
			mapper.map(src -> src.getType(), Intervention::setEnumType);
		});
		
		myMapper.typeMap(Intervention.class, InterventionDto.class).addMappings(mapper -> {
			mapper.map(src -> src.getEnumType(), InterventionDto::setType);
		});
		
		return myMapper.map(obj, clazz);
	}
}