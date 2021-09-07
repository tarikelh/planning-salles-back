package fr.dawan.calendarproject.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.dawan.calendarproject.dto.AdvancedUserDto;
import fr.dawan.calendarproject.dto.UserDto;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.services.LocationServiceImpl;
import fr.dawan.calendarproject.services.UserServiceImpl;

@Mapper(componentModel = "spring", uses = { UserServiceImpl.class, SkillMapperImpl.class, LocationMapperImpl.class,
		LocationServiceImpl.class })

public interface UserMapper {

	List<Long> setUsersToListLong(Set<User> users);
	
	Long userToLong(User user);

	Set<User> listLongToSetUsers(List<Long> ids);

	@Mapping(source = "skills", target = "skillsId")
	@Mapping(source = "location.id", target = "locationId")
	@Mapping(source = "enumType", target = "type")
	@Mapping(source = "enumCompany", target = "company")
	AdvancedUserDto userToAdvancedUserDto(User user);

	@Mapping(source = "locationId", target = "location")
	@Mapping(source = "skillsId", target = "skills")
	@Mapping(source = "type", target = "enumType")
	@Mapping(source = "company", target = "enumCompany")
	User advancedUserDtoToUser(AdvancedUserDto advUser);

	@Mapping(target = "skills", ignore = true)
	@Mapping(source = "locationId", target = "location")
	@Mapping(source = "type", target = "enumType")
	@Mapping(source = "company", target = "enumCompany")
	User UserDtoToUser(UserDto userDto);

	@Mapping(source = "location.id", target = "locationId")
	@Mapping(source = "enumType", target = "type")
	@Mapping(source = "enumCompany", target = "company")
	UserDto userToUserDto(User user);
}
