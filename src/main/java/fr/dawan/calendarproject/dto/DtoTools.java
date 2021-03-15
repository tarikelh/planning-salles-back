package fr.dawan.calendarproject.dto;

import org.modelmapper.ModelMapper;

public class DtoTools {

	public static <TSource, TDestination> TDestination convert(TSource obj, Class<TDestination> clazz) {
		ModelMapper myMapper = new ModelMapper(); // TODO ajout de config pour personnaliser le mapping dto<>entity
		// myMapper.typeMap(Contact.class, ContactDto.class)
		// .addMappings(mapper->{
		// mapper.map(src->src.getName(), ContactDto::setName);
		// mapper.map(src->src.getEmail(), ContactDto::setEmail);
		// });
		
		
//		 myMapper.typeMap(Intervention.class, InterventionDto.class)
//				 .addMappings(mapper->{
//				 mapper.map(src->src.getId(), InterventionDto::setId);
//				 mapper.map(src->src.getDateStart(), InterventionDto::setDateStart);
//				 mapper.map(src->src.getDateEnd(), InterventionDto::setDateEnd);
//				 mapper.map(src->src.getEmployee(), InterventionDto::setEmployee);
//				 mapper.map(src->src.getCourse(), InterventionDto::setCourse);
//				 });
		return myMapper.map(obj, clazz);
	}
}