package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;

import fr.dawan.calendarproject.dto.ResourceDto;
import fr.dawan.calendarproject.dto.RoomDto;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Resource;
import fr.dawan.calendarproject.entities.Room;
import fr.dawan.calendarproject.mapper.RoomMapper;
import fr.dawan.calendarproject.repositories.RoomRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class RoomServiceTest {
	
	@Autowired
	private RoomService roomService;
	
	@MockBean
	private RoomRepository roomRepository;
	
	@MockBean
	private RoomMapper roomMapper;
	
	@MockBean
	private RestTemplate restTemplate;
	
	private List<Room> rList = new ArrayList<Room>();
	private List<RoomDto> rDtoList =new ArrayList<RoomDto>();
	
	private Optional<Room> opRoom;
	
	@BeforeEach
	void beforeEach()throws Exception{
		
		Location location = new Location (1,"Paris","FR","",1,0);
		
		rList.add (new Room(1,1,"Room 1",25,true,0, location));
		rList.add (new Room(2,2,"Room 2",25,true,0, location));
		rList.add (new Room(3,3,"Room 3",25,true,0, location));

		rDtoList.add(new RoomDto(1, 1, "Room 1", 25 , true , 1 , 0 ));
		rDtoList.add(new RoomDto(2, 2, "Room 2", 25 , true , 1 , 0 ));
		rDtoList.add(new RoomDto(3, 3, "Room 3", 25 , true , 1 , 0 ));
		
		opRoom = Optional.of(rList.get(0));
	}
	@Test
	void contextLoads() {
		assertThat(roomService).isNotNull();
	}
	@Test
	void shouldFetchAllResourceAndReturnDtos() {
		when(roomRepository.findAll()).thenReturn(rList);
		when(roomMapper.roomToRoomDto(any(Room.class))).thenReturn(rDtoList.get(0), rDtoList.get(1),
				rDtoList.get(2));

		List<RoomDto> result = roomService.getAllRooms();

		assertThat(result).isNotNull();
		assertEquals(rDtoList.size(), result.size());
		assertEquals(rDtoList, result);
	}
	@Test 
	void shouldFetchAllRoomAndReturnPaginatedDtos() {
		Page<Room> p1 = new PageImpl<Room>(rList.subList(0, 2));
		when(roomRepository.findAllByLocationNameContaining(any(String.class), any(Pageable.class))).thenReturn(p1);
		when(roomMapper.roomToRoomDto(any(Room.class))).thenReturn(rDtoList.get(0), rDtoList.get(1));
		
		List<RoomDto> result = roomService.getAllRooms(0,2,"");
		assertThat(result).isNotNull();
		assertEquals(rList.subList(0, 2).size(), result.size());
		
	}
	
	@Test 
	void shouldFetchAllRoomWithPageAndSizeLessThanZero() {
		Page<Room> unpagedRoom = new PageImpl<Room>(rList);
		
		when(roomRepository.findAllByLocationNameContaining(any(String.class),any(Pageable.class)))
		.thenReturn(unpagedRoom);
		
		when(roomMapper.roomToRoomDto(any(Room.class))).thenReturn(rDtoList.get(0), rDtoList.get(1));
		
		List<RoomDto> result = roomService.getAllRooms(0,2,"");
		
		assertThat(result).isNotNull();
		assertEquals(rList.size(), result.size());
	}
	
	@Test
	void shouldGetResourceById() {
		when(roomRepository.findById(any(long.class))).thenReturn(Optional.of(rList.get(1)));
		when(roomMapper.roomToRoomDto(any(Room.class))).thenReturn(rDtoList.get(1));

		RoomDto result = roomService.getById(2);

		assertThat(result).isNotNull();
		assertEquals(rDtoList.get(1), result);
	}
}
			
