package fr.dawan.calendarproject.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.dawan.calendarproject.dto.LocationDG2Dto;
import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.mapper.LocationMapper;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class LocationMapperTest {

	@Autowired
	LocationMapper locationMapper;

	Location location = new Location();
	LocationDto locationDto = new LocationDto();
	LocationDG2Dto locationDG2Dto = new LocationDG2Dto();

	@BeforeEach
	void before() {
		locationDG2Dto = new LocationDG2Dto(3, "ville", true, "FR", "#00CC99");
		locationDto = new LocationDto(1, 1, "Paris", "#FFFFF", "FR",false, 0);
		location = new Location(2, "Sirap", "#00000", "FR", false, 1);
	}

	@Test
	void should_map_locationToLocationDto() {
		// mapping
		LocationDto mappedLocationDto = locationMapper.locationToLocationDto(location);

		// assert
		assertEquals(mappedLocationDto.getId(), location.getId());
		assertEquals(mappedLocationDto.getCity(), location.getCity());
		assertEquals(mappedLocationDto.getColor(), location.getColor());
		assertEquals(mappedLocationDto.getVersion(), location.getVersion());
	}

	@Test
	void should_map_locationDtoToLocation() {
		// mapping
		Location mappedLocation = locationMapper.locationDtoToLocation(locationDto);

		// assert
		assertEquals(mappedLocation.getId(), locationDto.getId());
		assertEquals(mappedLocation.getCity(), locationDto.getCity());
		assertEquals(mappedLocation.getColor(), locationDto.getColor());
		assertEquals(mappedLocation.getVersion(), locationDto.getVersion());
	}

	@Test
	void should_map_locationDG2DtoToLocation() {
		// mapping
		Location mappedLocation = locationMapper.locationDG2DtoToLocation(locationDG2Dto);

		// assert
		assertEquals(mappedLocation.getCity(), locationDG2Dto.getName());
	}
}
