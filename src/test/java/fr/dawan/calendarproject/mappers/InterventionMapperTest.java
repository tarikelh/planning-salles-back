package fr.dawan.calendarproject.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.mapper.InterventionMapper;
import fr.dawan.calendarproject.services.CourseService;
import fr.dawan.calendarproject.services.InterventionService;
import fr.dawan.calendarproject.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class InterventionMapperTest {
	
	@Autowired
	private InterventionMapper interventionMapper;
	
	@Autowired
	private InterventionService interventionService;
	
	private InterventionDto intervDto = new InterventionDto();
	private Intervention interv = new Intervention();

	@BeforeEach
	public void before() throws Exception {
		intervDto = interventionService.getById(1);
		interv = interventionService.getEntityById(1);
	}
	
	@Test
	void should_Map_InterventionDto_To_Intervention() {
		// Mapping
		Intervention myInterv = interventionMapper.interventionDtoToIntervention(intervDto);
		
		// assert
		assertNotNull(myInterv.getCourse());
		assertNotNull(myInterv.getLocation());
		assertNotNull(myInterv.getUser());
		
		assertNotNull(myInterv.getCourse().getTitle());
		assertNotNull(myInterv.getLocation().getCity());
		assertNotNull(myInterv.getUser().getEmail());
		
		assertEquals(myInterv.getComment(), intervDto.getComment());
		assertEquals(myInterv.getCourse().getId(), intervDto.getCourseId());
		assertEquals(myInterv.getDateEnd(), intervDto.getDateEnd());
		assertEquals(myInterv.getDateStart(), intervDto.getDateStart());
		assertEquals(myInterv.getId(), intervDto.getId());
		assertEquals(myInterv.getLocation().getId(), intervDto.getLocationId());
		assertEquals(myInterv.getTimeEnd(), intervDto.getTimeEnd());
		assertEquals(myInterv.getTimeStart(), intervDto.getTimeStart());	
		assertEquals(myInterv.getType().toString(), intervDto.getType());
		assertEquals(myInterv.getUser().getId(), intervDto.getUserId());
		assertEquals(myInterv.getVersion(), intervDto.getVersion());
		
		if(intervDto.getMasterInterventionId() > 0) {
			assertNotNull(myInterv.getMasterIntervention());
			assertNotNull(myInterv.getMasterIntervention().getComment());
			assertEquals(myInterv.getMasterIntervention().getId(), intervDto.getMasterInterventionId());
		}
	}

	@Test
	void should_Map_Intervention_To_InterventionDto() {
		// Mapping
		InterventionDto MyIntervDto = interventionMapper.interventionToInterventionDto(interv);
		
		// assert
		assertEquals(MyIntervDto.getComment(), interv.getComment());
		assertEquals(MyIntervDto.getCourseId(), interv.getCourse().getId());
		assertEquals(MyIntervDto.getDateEnd(), interv.getDateEnd());
		assertEquals(MyIntervDto.getDateStart(), interv.getDateStart());
		assertEquals(MyIntervDto.getId(), interv.getId());
		assertEquals(MyIntervDto.getLocationId(), interv.getLocation().getId());
		assertEquals(MyIntervDto.getTimeEnd(), interv.getTimeEnd());
		assertEquals(MyIntervDto.getTimeStart(), interv.getTimeStart());	
		assertEquals(MyIntervDto.getType(), interv.getType().toString());
		assertEquals(MyIntervDto.getUserId(), interv.getUser().getId());
		assertEquals(MyIntervDto.getVersion(), interv.getVersion());
		
		if(interv.getMasterIntervention() != null) {
			assertNotNull(MyIntervDto.getMasterInterventionId());
		}
	}
}
