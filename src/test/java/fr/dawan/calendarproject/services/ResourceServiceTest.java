package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Room;
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
import org.springframework.web.client.RestTemplate;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.ResourceDto;
import fr.dawan.calendarproject.entities.Resource;
import fr.dawan.calendarproject.mapper.ResourceMapper;
import fr.dawan.calendarproject.repositories.ResourceRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class ResourceServiceTest {

	@Autowired
	private ResourceService resourceService;

	@MockBean
	private ResourceRepository resourceRepository;

	@MockBean
	private ResourceMapper resourceMapper;

	@MockBean
	private RestTemplate restTemplate;

	private List<Resource> rList = new ArrayList<Resource>();
	private List<ResourceDto> rDtoList = new ArrayList<ResourceDto>();
	private Optional<Resource> opResource = null;

	@BeforeEach
	void beforeEach() throws Exception {
		
		Location location = new Location(1, "Paris", "FR", "red", 1, 0);
		
		Room room = new Room(1,1,"Room 1",25,true,0, location);
		
		rList.add(new Resource(1, "Casque", 10, room, 0));
		rList.add(new Resource(2, "Ecran", 10, room, 0));
		rList.add(new Resource(3, "Table", 10, room, 0));

		rDtoList.add(new ResourceDto(1, 0, 10, "Casque", 1 ));
		rDtoList.add(new ResourceDto(2, 0, 10, "Ecran", 1 ));
		rDtoList.add(new ResourceDto(3, 0, 10, "Table", 1 ));

		opResource = Optional.of(rList.get(0));
	}

	@Test
	void contextLoads() {
		assertThat(resourceService).isNotNull();
	}

	@Test
	void shouldFetchAllResourceAndReturnDtos() {
		when(resourceRepository.findAll()).thenReturn(rList);
		when(resourceMapper.resourceToResourceDto(any(Resource.class))).thenReturn(rDtoList.get(0), rDtoList.get(1),
				rDtoList.get(2));

		List<ResourceDto> result = resourceService.getAllResources();

		assertThat(result).isNotNull();
		assertEquals(rDtoList.size(), result.size());
		assertEquals(rDtoList, result);
	}

	@Test
	void shouldFetchAllResourceAndReturnPaginatedDtos() {
		Page<Resource> p1 = new PageImpl<Resource>(rList.subList(0, 2));

		when(resourceRepository.findAllByNameContaining(any(String.class), any(Pageable.class))).thenReturn(p1);
		when(resourceMapper.resourceToResourceDto(any(Resource.class))).thenReturn(rDtoList.get(0), rDtoList.get(1));

		List<ResourceDto> result = resourceService.getAllResources(0, 2, "");

		assertThat(result).isNotNull();
		assertEquals(rList.subList(0, 2).size(), result.size());
	}

	@Test
	void shouldFetchAllResourcesWithPageAndSizeLessThanZero() {
		Page<Resource> unpagedResources = new PageImpl<Resource>(rList);

		when(resourceRepository.findAllByNameContaining(any(String.class), any(Pageable.class)))
				.thenReturn(unpagedResources);
		when(resourceMapper.resourceToResourceDto(any(Resource.class))).thenReturn(rDtoList.get(0), rDtoList.get(1));

		List<ResourceDto> result = resourceService.getAllResources(0, 2, "");

		assertThat(result).isNotNull();
		assertEquals(rList.size(), result.size());
	}

	@Test
	void shouldGetResourceById() {
		when(resourceRepository.findById(any(long.class))).thenReturn(Optional.of(rList.get(1)));
		when(resourceMapper.resourceToResourceDto(any(Resource.class))).thenReturn(rDtoList.get(1));

		ResourceDto result = resourceService.getById(2);

		assertThat(result).isNotNull();
		assertEquals(rDtoList.get(1), result);
	}
	
	@Test
	void shouldGetResourceByQuantity() {
		when(resourceRepository.findByQuantity(any(int.class))).thenReturn(rList);
		when(resourceMapper.resourceToResourceDto(any(Resource.class))).thenReturn(rDtoList.get(1));

		List<ResourceDto> result = resourceService.getResourceByQuantity(10);

		assertThat(result).isNotNull();
		assertEquals(rDtoList.get(1), result.get(1));
	}

	@Test
	void shouldGetResourceByQuantityRange() {
		when(resourceRepository.findByQuantityRange(any(int.class),any(int.class))).thenReturn(rList);
		when(resourceMapper.resourceToResourceDto(any(Resource.class))).thenReturn(rDtoList.get(1));

		List<ResourceDto> result = resourceService.getResourceByQuantityRange(8,12);

		assertThat(result).isNotNull();
		assertEquals(rDtoList.get(1), result.get(1));
	}

	@Test
	void shouldGetResourceByRoomAvailability() {
		when(resourceRepository.findByRoomAvailability(any(boolean.class))).thenReturn(rList);
		when(resourceMapper.resourceToResourceDto(any(Resource.class))).thenReturn(rDtoList.get(1));

		List<ResourceDto> result = resourceService.getResourceByRoomAvailability(true);

		assertThat(result).isNotNull();
		assertEquals(rDtoList.get(1), result.get(1));
	}

	@Test
	void shouldReturnNullWhenResourceIdIsUnknown() {
		when(resourceRepository.findById(any(long.class))).thenReturn(Optional.empty());

		ResourceDto result = resourceService.getById(2);

		assertThat(result).isNull();
	}

	@Test
	void testSaveNewResource() {

		Location location = new Location(1, "Paris", "FR", "red", 1, 0);

		Room room = new Room(1,1,"Room 1",25,true,0, location);
		
		ResourceDto toCreate = new ResourceDto(0, 0, 10, "Casque", room.getId() );
		Resource repoReturn = new Resource(4, "Casque", 10, room, 0 );
		ResourceDto expected = new ResourceDto(4, 0, 10, "Casque", room.getId() );

		when(resourceMapper.resourceDtoToResource(any(ResourceDto.class))).thenReturn(repoReturn);
		when(resourceRepository.saveAndFlush(any(Resource.class))).thenReturn(repoReturn);
		when(resourceMapper.resourceToResourceDto(any(Resource.class))).thenReturn(expected);

		ResourceDto result = resourceService.saveOrUpdate(toCreate);

		assertThat(result).isNotNull();
		assertEquals(expected, result);
	}

	@Test
	void ShouldReturnNullWhenUpdateResourceWithWrongId() {
		ResourceDto toUpdate = new ResourceDto(1111, 0, 10, "Casque", 1);

		when(resourceRepository.findById(any(long.class))).thenReturn(Optional.empty());

		ResourceDto result = resourceService.saveOrUpdate(toUpdate);

		assertThat(result).isNull();
	}

	@Test
	void shouldReturnTrueWhenResourceIsUniq() {
		ResourceDto goodResource = new ResourceDto(4, 0, 10, "Casque", 1);

		when(resourceRepository.findByQuantity(any(int.class)))
				.thenReturn(new ArrayList<Resource>());

		boolean result = resourceService.equals(goodResource);

		assertThat(result).isFalse();
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	void shouldReturnFalseWhenResourceIsNotUniq() {
		Location location = new Location(1, "Paris", "FR", "red", 1, 0);
		
		Room room = new Room(1,1,"Room 1",25,true,0, location);
		
		List<Resource> dupRes = new ArrayList<Resource>();
		dupRes.add(rList.get(0));

		ResourceDto badResource = new ResourceDto(1111, 0, 10, "Casque", room.getId());

		when(resourceRepository.findDuplicateByName(any(Long.class), any(Long.class), any(String.class)))
				.thenReturn(dupRes);

		assertFalse(rList.get(0).equals(badResource));
	}

	@Test
	void shouldReturnCount() {
		when(resourceRepository.countByNameContaining(any(String.class))).thenReturn((long) rList.size());

		CountDto result = resourceService.count("");

		assertEquals(rList.size(), result.getNb());
	}

}
