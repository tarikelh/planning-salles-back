package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import fr.dawan.calendarproject.dto.AdvancedInterventionDto;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.CourseDto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.dto.InterventionFollowedDG2Dto;
import fr.dawan.calendarproject.dto.InterventionFollowedDto;
import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.dto.UserDto;
import fr.dawan.calendarproject.entities.Course;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.InterventionFollowed;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.InterventionStatus;
import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.mapper.InterventionFollowedMapper;
import fr.dawan.calendarproject.repositories.InterventionFollowedRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class InterventionFollowedServiceTest {

    @Autowired
    private InterventionFollowedService interventionFollowedService;

    @MockBean
    private InterventionFollowedRepository interventionFollowedRepository;

    @MockBean
    private InterventionFollowedMapper interventionFollowedMapper;

    @MockBean
    private InterventionRepository interventionRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RestTemplate restTemplate;

    private Location location = new Location();
    private LocationDto locationDto = new LocationDto();  
    private User user = new User();   
    private Course course = new Course(); 
    private CourseDto courseDto = new CourseDto(); 
    private UserDto userDto = new UserDto();
    private Intervention masterIntervention = new Intervention(); 
    private Intervention intervention = new Intervention();   
    private InterventionDto interventionDto = new InterventionDto();
    private AdvancedInterventionDto advInterventionDto = new AdvancedInterventionDto();
    private List<InterventionFollowed> interventionsFollowed = new ArrayList<>();
    private List<InterventionFollowedDto> interventionsFollowedDtos = new ArrayList<>();

    @BeforeEach
    void beforeEach() throws Exception {
        
        location = new Location(1L, "Paris", "FR", "red", true, 0);
        locationDto = new LocationDto(1, 1, "Paris", "FR", "red", true, 0);
        
        user = new User(1L, 1L, 3L, "Daniel", "Balavoine", location, "dbalavoine@dawan.fr", new HashSet<Skill>(),
                UserType.ADMINISTRATIF, UserCompany.DAWAN, "userImage.png", LocalDate.now(), new HashSet<InterventionFollowed>(), 0);
        
        course = new Course(1, 1, "Java course for beginners", 5, "slug", 0);
        
        userDto = new UserDto(1L, 1L, 1L, "Daniel", "Balavoine", location.getId(), "dbalavoine@dawan.fr",
                "testPassword", UserType.ADMINISTRATIF.toString(), UserCompany.DAWAN.toString(), "userImage.png", 0);
        courseDto = new CourseDto(1, 1, "Java course for beginners", 5, "slug", 0);

        masterIntervention = new Intervention(1L, 1L, "masterSlug", "master optionSlug", "I am a master Intervention", location,
                course, user, 3, InterventionStatus.INTERN, true, LocalDate.now().plusDays(7),
                LocalDate.now().plusDays(10), LocalTime.of(9, 0), LocalTime.of(17, 0), null, true, 3L, "", new HashSet<InterventionFollowed>(), 0);
        
        intervention = new Intervention(2L, 2L, "intervention slug", "intervention optionSlug", "intervention comment", location, course,
                user, 10, InterventionStatus.TP, true, LocalDate.now(), LocalDate.now().plusDays(5),
                LocalTime.of(9, 0),
                LocalTime.of(17, 0), masterIntervention, false, 0, "MARCEAU Sophie, GOLDMAN Jean-Jacques", new HashSet<InterventionFollowed>(), 0);
        
        interventionDto = new InterventionDto(2L, 2L, "intervention slug", "intervention comment", location.getId(), location.getIdDg2(),
                course.getId(), course.getIdDg2(), user.getId(), 10, InterventionStatus.TP.toString(), true, LocalDate.now(), LocalDate.now().plusDays(5),
                LocalTime.of(9, 0),
                LocalTime.of(17, 0), 1L, false, "MARCEAU Sophie, GOLDMAN Jean-Jacques", 0);

        advInterventionDto = new AdvancedInterventionDto(1L, 1L, "advInter slug",
                "advInter optionSlug", "advInter comment",
                locationDto,
                courseDto, userDto, 1, InterventionStatus.INTERN.toString(), true, LocalDate.now(),
                LocalDate.now().plusDays(1L),
                LocalTime.now(), LocalTime.now().plusHours(7L), interventionDto, true,
                "MARCEAU Sophie, GOLDMAN Jean-Jacques", 1);

        interventionsFollowed.add(new InterventionFollowed(1, user, intervention, "registration Slug1", 0));
        interventionsFollowed.add(new InterventionFollowed(2, user, intervention, "registration Slug2", 0));
        interventionsFollowed.add(new InterventionFollowed(3, user, intervention, "registration Slug3", 0));
        interventionsFollowed.add(new InterventionFollowed(4, user, intervention, "registration Slug4", 0));

        interventionsFollowedDtos.add(new InterventionFollowedDto(1, userDto, advInterventionDto, "registration Slug1", 0));
        interventionsFollowedDtos.add(new InterventionFollowedDto(2, userDto, advInterventionDto, "registration Slug2", 0));
        interventionsFollowedDtos.add(new InterventionFollowedDto(3, userDto, advInterventionDto, "registration Slug3", 0));
        interventionsFollowedDtos.add(new InterventionFollowedDto(4, userDto, advInterventionDto, "registration Slug4", 0));
        

    }

//    @AfterEach
//    public void afterEach() throws Exception {
//        if (!mICalTools.isClosed())
//            mICalTools.close();
//    }

    @Test
    void contextLoads() {
        assertThat(interventionFollowedService).isNotNull();
    }

    @Test
    void shouldGetInterventionsAndReturnDtos() {
        when(interventionFollowedRepository.findAll()).thenReturn(interventionsFollowed);
        when(interventionFollowedMapper.listInterventionFollowedToListInterventionFollowedDto(interventionsFollowed)).thenReturn(interventionsFollowedDtos);

        List<InterventionFollowedDto> result = interventionFollowedService.getAllInterventionsFollowed();

        assertThat(result).isNotNull();
        assertEquals(interventionsFollowed.size(), result.size());
        assertEquals(interventionsFollowedDtos.size(), result.size());
        assertEquals(interventionsFollowedDtos, result);
    }

    @Test
    void shouldGetPaginatedInterventionsAndReturnDtos() {
        int page = 1;
        int size = 2;

        Page<InterventionFollowed> p1 = new PageImpl<InterventionFollowed>(interventionsFollowed.subList(0, 2));
        Page<InterventionFollowed> p2 = new PageImpl<InterventionFollowed>(interventionsFollowed.subList(2, 4));

        when(interventionFollowedRepository.findAll(PageRequest.of(page, size))).thenReturn(p1);
        when(interventionFollowedRepository.findAll(PageRequest.of(page + 1, size))).thenReturn(p2);
        when(interventionFollowedMapper.listInterventionFollowedToListInterventionFollowedDto(p1.get()
                .collect(Collectors.toList()))).thenReturn(interventionsFollowedDtos.subList(0, 2));
        when(interventionFollowedMapper.listInterventionFollowedToListInterventionFollowedDto(p2.get()
                .collect(Collectors.toList()))).thenReturn(interventionsFollowedDtos.subList(2, 4));

        List<InterventionFollowedDto> page1 = interventionFollowedService.getAllInterventionsFollowed(page, size);
        List<InterventionFollowedDto> page2 = interventionFollowedService.getAllInterventionsFollowed(page + 1, size);
        
        assertThat(page1).isNotNull();
        assertThat(page2).isNotNull();
        assertEquals(2, page1.size());
        assertEquals(2, page2.size());
        assertEquals(page1, interventionsFollowedDtos.subList(0, 2));
        assertEquals(page2, interventionsFollowedDtos.subList(2, 4));
    }

    @Test
    void shouldThrowIllegalArgumentWhenPageIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            interventionFollowedService.getAllInterventionsFollowed(-1, 1);
        });
    }

    @Test
    void shouldThrowIllegalArgumentWhenSizeIsLessThanOne() {
        assertThrows(IllegalArgumentException.class, () -> {
            interventionFollowedService.getAllInterventionsFollowed(0, 0);
        });
    }
    
    @Test
    void shouldReturnCount() {
        String userType = UserType.ADMINISTRATIF.toString();
        when(interventionFollowedRepository.findByUserType(UserType.ADMINISTRATIF)).thenReturn(interventionsFollowed);

        CountDto result = interventionFollowedService.count(userType);

        assertEquals(interventionsFollowed.size(), result.getNb());
    }
    
    @Test
    void shouldGetInterventionFollowedById() {
        long interventionFollowedId = 2;
        InterventionFollowedDto expected = interventionsFollowedDtos.get(1);

        when(interventionFollowedRepository.findById(interventionFollowedId)).thenReturn(Optional.of(interventionsFollowed.get(1)));
        when(interventionFollowedMapper.interventionFollowedToInterventionFollowedDto(any(InterventionFollowed.class))).thenReturn(interventionsFollowedDtos.get(1));

        InterventionFollowedDto result = interventionFollowedService.getById(interventionFollowedId);

        assertThat(result).isNotNull();
        assertEquals(expected, result);
        assertEquals(interventionFollowedId, result.getId());
    }

    @Test
    void shouldReturnNullWhenGetByIdWhenWrongIdProvided() {
        long interventionFollowedId = 0;

        when(interventionRepository.findById(interventionFollowedId)).thenReturn(Optional.empty());
        assertThat(interventionFollowedService.getById(interventionFollowedId)).isNull();
    }
    
    @Test
    void shouldDeleteInterventionFollowed() throws Exception {

        doNothing().when(interventionFollowedRepository).deleteById(any(Long.class));
        assertDoesNotThrow(() -> interventionFollowedService.deleteById(any(Long.class)));
    }
    
    @Test
    void shouldSaveNewInterventionFollowed() throws Exception {

        InterventionFollowedDto  newInterventionFollowedDto = new InterventionFollowedDto(0, userDto, advInterventionDto, "registration Slug to test", 0);
        
        InterventionFollowed  newInterventionFollowed = new InterventionFollowed(1, user, intervention, "registration Slug to test", 0);
        
        when(interventionFollowedMapper.interventionFollowedDtoToInterventionFollowed(newInterventionFollowedDto)).thenReturn(newInterventionFollowed);
        when(interventionRepository.findById(newInterventionFollowedDto.getInterventionId())).thenReturn(Optional.of(intervention));
        when(userRepository.findById(newInterventionFollowedDto.getUserId())).thenReturn(Optional.of(user));
        when(interventionFollowedRepository.saveAndFlush(newInterventionFollowed)).thenReturn(newInterventionFollowed);
        when(interventionFollowedMapper.interventionFollowedToInterventionFollowedDto(newInterventionFollowed)).thenReturn(newInterventionFollowedDto);

        InterventionFollowedDto result = interventionFollowedService.saveOrUpdate(newInterventionFollowedDto);
        
        assertThat(result).isNotNull();
        assertEquals(newInterventionFollowedDto, result); 

    }
    
    @Test
    void shouldUpdateInterventionFollowed() throws Exception {
        InterventionFollowed  interventionFollowed = new InterventionFollowed(1, user, intervention, "registrationSlugtoTest", 0);
        InterventionFollowedDto  interventionFollowedDto = new InterventionFollowedDto(1, userDto, advInterventionDto, "registration Slug to Test", 0);        
        
        when(interventionFollowedMapper.interventionFollowedDtoToInterventionFollowed(interventionFollowedDto)).thenReturn(interventionFollowed);
        when(userRepository.findById(interventionFollowedDto.getUserId())).thenReturn(Optional.of(user));
        when(interventionRepository.findById(interventionFollowedDto.getInterventionId())).thenReturn(Optional.of(intervention));
        when(interventionFollowedRepository.findById(interventionFollowedDto.getId())).thenReturn(Optional.of(interventionsFollowed.get(1)));
        when(interventionFollowedRepository.saveAndFlush(interventionFollowed)).thenReturn(interventionFollowed);
        when(interventionFollowedMapper.interventionFollowedToInterventionFollowedDto(interventionFollowed)).thenReturn(interventionFollowedDto);       
        
        InterventionFollowedDto result = interventionFollowedService.saveOrUpdate(interventionFollowedDto);

        assertThat(result).isNotNull();
        assertEquals(result, interventionFollowedDto);
    }
    
    @Test
    void shouldGetAllInterventionFollowedByInterventionId() {
        when(interventionFollowedRepository.findByInterventionId(any(long.class))).thenReturn(interventionsFollowed);
        when(interventionFollowedMapper.interventionFollowedToInterventionFollowedDto(any(InterventionFollowed.class))).thenReturn(interventionsFollowedDtos.get(1));

        List<InterventionFollowedDto> result = interventionFollowedService.findAllByInterventionId(intervention.getId());

        assertThat(result).isNotNull();
    }
    
    @Test
    void shouldGetAllInterventionFollowedByUserType() {
        when(interventionFollowedRepository.findByUserType(any(UserType.class))).thenReturn(interventionsFollowed);
        when(interventionFollowedMapper.interventionFollowedToInterventionFollowedDto(any(InterventionFollowed.class))).thenReturn(interventionsFollowedDtos.get(1));

        List<InterventionFollowedDto> result = interventionFollowedService.findAllByUserType(UserType.ADMINISTRATIF.toString());

        assertThat(result).isNotNull();
    }

    @Test
    void shouldGetAllInterventionFollowedByUserTypeAndDateRange() {
        when(interventionFollowedRepository.getAllByUserTypeAndDateRange(any(UserType.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(interventionsFollowed);
        when(interventionFollowedMapper.interventionFollowedToInterventionFollowedDto(any(InterventionFollowed.class))).thenReturn(interventionsFollowedDtos.get(1));

        List<InterventionFollowedDto> result = interventionFollowedService.findAllByUserTypeAndDateRange(UserType.ADMINISTRATIF.toString(), LocalDate.now(), LocalDate.now().plusDays(5));

        assertThat(result).isNotNull();
    }

    @Test
    void shouldGetAllInterventionFollowedByDateRange() {
        when(interventionFollowedRepository.findAllByDateRange(any(LocalDate.class), any(LocalDate.class))).thenReturn(interventionsFollowed);
        when(interventionFollowedMapper.interventionFollowedToInterventionFollowedDto(any(InterventionFollowed.class))).thenReturn(interventionsFollowedDtos.get(1));

        List<InterventionFollowedDto> result = interventionFollowedService.findAllByDateRange(LocalDate.now(), LocalDate.now().plusDays(5));

        assertThat(result).isNotNull();
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldGetInterventionsFollowedFromDG2() throws Exception {
        String body ="[{\"interventionId\": 603737,\"personId\": 156994,\"employeeId\": 164,\"registrationId\": 104247,\"registrationSlug\": \"gachet-raphael-1--gachet-raphael-1\"},\r\n"
                + "{\"interventionId\": 603737,\"personId\": 156035,\"employeeId\": 159,\"registrationId\": 104250,\"registrationSlug\": \"ravaille-valentin--ravaille-valentin\"},\r\n"
                + "{\"interventionId\": 603737,\"personId\": 98000,\"employeeId\": 119,\"registrationId\": 104249,\"registrationSlug\": \"plages-aimeric--plages-aimeric\"},\r\n"
                + "{\"interventionId\": 603737,\"personId\": 180553,\"employeeId\": 278,\"registrationId\": 104248,\"registrationSlug\": \"girardeau-david-2--girardeau-david-2\"},\r\n"
                + "{\"interventionId\": 603736,\"personId\": 113582,\"employeeId\": 130,\"registrationId\": 104252,\"registrationSlug\": \"guilbert-jean-francois--guilbert-jean-francois\"},\r\n"
                + "{\"interventionId\": 603736,\"personId\": 183766,\"employeeId\": 290,\"registrationId\": 104251,\"registrationSlug\": \"berra-claire--berra-claire\"},\r\n"
                + "{\"interventionId\": 603769,\"personId\": 183450,\"employeeId\": 284,\"registrationId\": 104304,\"registrationSlug\": \"gautier-jean-francois-2--gautier-jean-francois-2\"},\r\n"
                + "{\"interventionId\": 603736,\"personId\": 111890,\"employeeId\": 127,\"registrationId\": 104253,\"registrationSlug\": \"perrot-solene--perrot-solene-1\"},\r\n"
                + "{\"interventionId\": 596341,\"personId\": 176021,\"employeeId\": 277,\"registrationId\": 104407,\"registrationSlug\": \"nabili-mohammed--nabili-mohammed-1\"},\r\n"
                + "{\"interventionId\": 602495,\"personId\": 192601,\"employeeId\": 311,\"registrationId\": 99006,\"registrationSlug\": \"abdelouhab-riad-1--abdelouhab-riad-1-2\"},\r\n"
                + "{\"interventionId\": 566855,\"personId\": 184743,\"employeeId\": 291,\"registrationId\": 104550,\"registrationSlug\": \"diatta-aliou-2--diatta-aliou-2\"},\r\n"
                + "{\"interventionId\": 596424,\"personId\": 176021,\"employeeId\": 277,\"registrationId\": 104466,\"registrationSlug\": \"nabili-mohammed--\"}]";
                
                ResponseEntity<String> res = new ResponseEntity<String>(body, HttpStatus.OK);
                when(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                        .thenReturn(res);
                when(interventionFollowedMapper.interventionFollowedDG2DtoToInterventionFollowed(any(InterventionFollowedDG2Dto.class))).thenReturn(interventionsFollowed.get(0));
                when(interventionFollowedRepository.findByRegistrationSlug(any(String.class))).thenReturn(Optional.of(interventionsFollowed.get(1)));
                when(interventionFollowedRepository.saveAndFlush(interventionsFollowed.get(0))).thenReturn(interventionsFollowed.get(0));

                assertDoesNotThrow(() -> interventionFollowedService.fetchAllDG2InterventionsFollowed("emailDG2", "passwordDG2", LocalDate.parse("2022-01-01"), LocalDate.parse("2022-01-31")));
    }
    
}
