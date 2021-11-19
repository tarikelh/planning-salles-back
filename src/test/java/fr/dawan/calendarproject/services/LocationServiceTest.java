package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.mapper.LocationMapper;
import fr.dawan.calendarproject.repositories.LocationRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class LocationServiceTest {
	
	@Autowired
	private LocationService locationService;
	
	@MockBean
	private LocationRepository locationRepository;
	
	@MockBean
	private LocationMapper locationMapper;

	private List<Location> lList = new ArrayList<Location>();
	private List<LocationDto> lDtoList = new ArrayList<LocationDto>();

	@BeforeEach
	void beforeEach() throws Exception {
		lList.add(new Location(1, "Paris", "red", 0));
		lList.add(new Location(2, "Nantes", "blue", 0));
		lList.add(new Location(3, "Lyon", "pink", 0));
		lList.add(new Location(4, "Montpellier", "green", 0));
		
		lDtoList.add(new LocationDto(1, "Paris", "red", 0));
		lDtoList.add(new LocationDto(2, "Nantes", "blue", 0));
		lDtoList.add(new LocationDto(3, "Lyon", "pink", 0));
		lDtoList.add(new LocationDto(4, "Montpellier", "green", 0));
	}

	@Test
	void contextLoads() {
		assertThat(locationService).isNotNull();
	}

	@Test
	void shouldFetchAllLocationAndReturnDtos() {
		when(locationRepository.findAll()).thenReturn(lList);
		when(locationMapper.locationToLocationDto(any(Location.class)))
			.thenReturn(lDtoList.get(0), lDtoList.get(1), lDtoList.get(2), lDtoList.get(3));
		
		List<LocationDto> result = locationService.getAllLocations();
		
		assertThat(result).isNotNull();
		assertEquals(lDtoList.size(), result.size());
		assertEquals(lDtoList, result);
	}

	@Test
	void shouldFetchAllLocationAndReturnPaginatedDtos() {
		Page<Location> p1 = new PageImpl<Location>(lList.subList(0, 2));
		
		when(locationRepository.findAllByCityContaining(any(String.class), any(Pageable.class))).thenReturn(p1);
		when(locationMapper.locationToLocationDto(any(Location.class)))
			.thenReturn(lDtoList.get(0), lDtoList.get(1));
		
		List<LocationDto> result = locationService.getAllLocations(0, 2, "");
		
		assertThat(result).isNotNull();
		assertEquals(lList.subList(0, 2).size(), result.size());
	}

	@Test
	void shouldFetchAllLocationsWithPageAndSizeLessThanZero() {
		Page<Location> unpagedSkills = new PageImpl<Location>(lList);

		when(locationRepository.findAllByCityContaining(any(String.class), any(Pageable.class)))
			.thenReturn(unpagedSkills);
		when(locationMapper.locationToLocationDto(any(Location.class)))
			.thenReturn(lDtoList.get(0), lDtoList.get(1));
		
		List<LocationDto> result = locationService.getAllLocations(0, 2, "");
		
		assertThat(result).isNotNull();
		assertEquals(lList.size(), result.size());
	}
	
	@Test
	void shouldGetLocationById() {
		when(locationRepository.findById(any(long.class))).thenReturn(Optional.of(lList.get(1)));
		when(locationMapper.locationToLocationDto(any(Location.class))).thenReturn(lDtoList.get(1));
		
		LocationDto result = locationService.getById(2);
		
		assertThat(result).isNotNull();
		assertEquals(lDtoList.get(1), result);
	}
	
	@Test
	void shouldReturnNullWhenLocationIdIsUnknown() {
		when(locationRepository.findById(any(long.class))).thenReturn(Optional.empty());
		
		LocationDto result = locationService.getById(2);
		
		assertThat(result).isNull();
	}
	
	@Test
	void testSaveNewLocation() {
		LocationDto toCreate = new LocationDto(0, "Toulouse", "brown", 0);
		Location repoReturn = new Location(5, "Toulouse", "brown", 0);
		LocationDto expected = new LocationDto(5, "Toulouse", "brown", 0);

		when(locationMapper.locationDtoToLocation(any(LocationDto.class)))
				.thenReturn(repoReturn);
		when(locationRepository.saveAndFlush(any(Location.class))).thenReturn(repoReturn);
		when(locationMapper.locationToLocationDto(any(Location.class))).thenReturn(expected);

		LocationDto result = locationService.saveOrUpdate(toCreate);

		assertThat(result).isNotNull();
		assertEquals(expected, result);
	}

	@Test
	void ShouldReturnNullWhenUpdateLocationWithWrongId() {
		LocationDto toUpdate = new LocationDto(1111, "Paris", "brown", 0);

		when(locationRepository.findById(any(long.class))).thenReturn(Optional.empty());

		LocationDto result = locationService.saveOrUpdate(toUpdate);

		assertThat(result).isNull();
	}
	
	@Test
	void shouldReturnTrueWhenLocationIsUniq() {
		LocationDto goodLocation = new LocationDto(5, "Toulouse", "brown", 0);
		
		when(locationRepository.findDuplicates(any(long.class), any(String.class), any(String.class)))
			.thenReturn(new ArrayList<Location>());
		
		boolean result = locationService.checkUniqness(goodLocation);
		
		assertThat(result).isTrue();
	}
	
	@Test
	void shouldThrowWhenLocationIsNotUniq() {
		List<Location> dupLoc = new ArrayList<Location>();
		dupLoc.add(lList.get(0));
		dupLoc.add(lList.get(1));
		
		LocationDto badLocation = new LocationDto(1111, "Paris", "blue", 0);
		
		when(locationRepository.findDuplicates(any(long.class), any(String.class), any(String.class)))
			.thenReturn(dupLoc);
		
		assertThrows(EntityFormatException.class, () -> {
			locationService.checkUniqness(badLocation);
		});
	}

	@Test
	void shouldReturnCount() {
		when(locationRepository.countByCityContaining(any(String.class)))
			.thenReturn((long) lList.size());
		
		CountDto result = locationService.count("");
		
		assertEquals(lList.size(), result.getNb());
	}

}
