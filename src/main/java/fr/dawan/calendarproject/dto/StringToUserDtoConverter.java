package fr.dawan.calendarproject.dto;

import org.springframework.core.convert.converter.Converter;


public class StringToUserDtoConverter implements Converter<String, AvancedUserDto> {

	@Override
	public AvancedUserDto convert(String source) {
		String[] myArray = source.split(",");
		AvancedUserDto dto = new AvancedUserDto();
		dto.setFirstName(myArray[0]);
		dto.setEmail(myArray[1]);
		return dto;
	}

}
