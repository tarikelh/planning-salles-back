package fr.dawan.calendarproject.dto;

import org.modelmapper.ModelMapper;

import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;

public class DtoTools {

	public static <TSource, TDestination> TDestination convert(TSource obj, Class<TDestination> clazz) {
		ModelMapper myMapper = new ModelMapper();
		
		myMapper.typeMap(Intervention.class, InterventionDto.class)
			.addMappings(mapper->{
				mapper.map(src->src.getUser().getId(), InterventionDto::setUserId);
				mapper.map(src->src.getLocation().getId(), InterventionDto::setLocationId);
				mapper.map(src->src.getCourse().getId(), InterventionDto::setCourseId);
				mapper.map(src->src.getCourse().getId(), InterventionDto::setCourseId);
			});

		myMapper.typeMap(User.class, AdvancedUserDto.class)
			.addMappings(mapper -> {
				mapper.map(src->src.getSkills().stream().map(s -> s.getId()).toArray(), AdvancedUserDto::setSkillsId);
			});
		
		myMapper.typeMap(Skill.class, AdvancedSkillDto.class)
		.addMappings(mapper -> {
			mapper.map(src->src.getUsers().stream().map(s -> s.getId()).toArray(), AdvancedSkillDto::setUsersId);
		});

		return myMapper.map(obj, clazz);
	}
}