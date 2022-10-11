package fr.dawan.calendarproject.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.UserDG2Dto;
import fr.dawan.calendarproject.dto.UserDto;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.repositories.LocationRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@Mapper(componentModel = "spring", uses = { UserRepository.class, SkillMapper.class, LocationRepository.class })

public interface UserMapper {

	List<Long> setUsersToListLong(Set<User> users);

	default Long getUserId(User user) {
		return user.getId();
	}

	Set<User> listUserToSetUser(List<User> users);

	Set<User> listLongToSetUsers(List<Long> ids);

	
	@Mapping(source = "skills", target = "skills")
	@Mapping(source = "location.id", target = "locationId")
	@Mapping(source = "enumType", target = "type")
	@Mapping(source = "enumCompany", target = "company")
	AdvancedUserDto userToAdvancedUserDto(User user);

	@Mapping(source = "locationId", target = "location")
	@Mapping(source = "skills", target = "skills")
	@Mapping(target = "interventionsFollowed", ignore = true)
	@Mapping(source = "type", target = "enumType")
	@Mapping(source = "company", target = "enumCompany")
	User advancedUserDtoToUser(AdvancedUserDto advUser);

	@Mapping(target = "skills", ignore = true)
	@Mapping(target = "interventionsFollowed", ignore = true)
	@Mapping(source = "locationId", target = "location")
	@Mapping(source = "type", target = "enumType")
	@Mapping(source = "company", target = "enumCompany")
	User userDtoToUser(UserDto userDto);

	@Named("userToUserDto")
	@Mapping(source = "location.id", target = "locationId")
	@Mapping(source = "enumType", target = "type")
	@Mapping(source = "enumCompany", target = "company")
	UserDto userToUserDto(User user);

	@Mapping(target = "imagePath", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "enumCompany", source = "company")
	@Mapping(target = "enumType", source = "type")
	@Mapping(target = "location", ignore = true)
	@Mapping(target = "skills", ignore = true)
	@Mapping(target = "interventionsFollowed", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "idDg2", source = "personId")
	@Mapping(target = "employeeIdDg2", source = "employeeId")
	User userDG2DtoToUser(UserDG2Dto cDG2);
}
