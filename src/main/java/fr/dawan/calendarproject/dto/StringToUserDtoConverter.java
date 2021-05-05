package fr.dawan.calendarproject.dto;

import org.springframework.core.convert.converter.Converter;


public class StringToUserDtoConverter implements Converter<String, AdvancedUserDto> {

	@Override
	public AdvancedUserDto convert(String source) {
		String[] myArray = source.split(",");
		AdvancedUserDto dto = new AdvancedUserDto();
		dto.setFirstName(myArray[0]);
		dto.setEmail(myArray[1]);
		return dto;
	}

}
