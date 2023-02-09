package fr.dawan.calendarproject.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.dawan.calendarproject.dto.CenterDto;
import fr.dawan.calendarproject.entities.Center;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.mapper.CenterMapper;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class CenterMapperTest {

	@Autowired
	private CenterMapper centerMapper;
	
	private Center center = new Center();
	private CenterDto centerDto = new CenterDto();
	private Location location = new Location();
	
	@BeforeEach
	void before(){
		location = new Location(1, "paris", "#FFFFFF", "FR", false, 1);
		//center = new Center(1,30,15,"adress","078124487",location,1);
		centerDto = new CenterDto(1,30,15,"adress","078124487",1,1);
		
	}
	
	@Test
	void should_map_centerToCenterDto() {
		//mapping
		CenterDto mappedCenterDto = centerMapper.centerToCenterDto(center);
		
		//assert
		assertEquals(mappedCenterDto.getId(), center.getId());
//		assertEquals(mappedCenterDto.getFullCapacity(), center.getFullCapacity());
//		assertEquals(mappedCenterDto.getReducedCapacity(), center.getReducedCapacity());
//		assertEquals(mappedCenterDto.getAdress(), center.getAdress());
		assertEquals(mappedCenterDto.getPhoneNumber(), center.getPhoneNumber());
		assertEquals(mappedCenterDto.getLocationId(), center.getLocation());
		assertEquals(mappedCenterDto.getVersion(), center.getVersion());
	}
	
	@Test
	void should_map_centerDtoToCenter() {
		//mapping
		Center mappedCenter = centerMapper.centerDtoToCenter(centerDto);
		
		//assert
		assertEquals(mappedCenter.getId(), centerDto.getId());
//		assertEquals(mappedCenter.getFullCapacity(), centerDto.getFullCapacity());
//		assertEquals(mappedCenter.getReducedCapacity(), centerDto.getReducedCapacity());
//		assertEquals(mappedCenter.getAdress(), centerDto.getAdress());
		assertEquals(mappedCenter.getPhoneNumber(), centerDto.getPhoneNumber());
		assertEquals(mappedCenter.getLocation(), centerDto.getLocationId());
		assertEquals(mappedCenter.getVersion(), centerDto.getVersion());
	}
		
}
