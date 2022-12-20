package fr.dawan.calendarproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.SkillDto;
import fr.dawan.calendarproject.entities.InterventionFollowed;
import fr.dawan.calendarproject.entities.Skill;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.mapper.SkillMapper;
import fr.dawan.calendarproject.repositories.SkillRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class SkillServiceTest {
	
	@Autowired
	private SkillService skillService;
	
	@MockBean
	private SkillRepository skillRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private SkillMapper skillMapper;
	
	private List<Skill> sList = new ArrayList<>();
	private List<SkillDto> sDtoList = new ArrayList<>();
	
	
	List<Long> userList = new ArrayList<Long>();
	
	@BeforeEach
	public void beforeEach() {
		
		userList.add(10L);
		userList.add(11L);
		
		sList.add(new Skill(1, "DevOps", null, 0));
		sList.add(new Skill(2, "POO", null, 0));
		sList.add(new Skill(3, "SQL", null, 0));
		
		sDtoList.add(new SkillDto(1, "DevOps", null, 0));
		sDtoList.add(new SkillDto(2, "POO", null, 0));
		sDtoList.add(new SkillDto(3, "SQL", null, 0));
		
	}

	@Test
	void contextLoads() {
		assertThat(skillService).isNotNull();
	}
	
	@Test
	void shouldGetSkillsAndReturnDtos() {
		when(skillRepository.findAll()).thenReturn(sList);
		when(skillMapper.skillToSkillDto(any(Skill.class)))
			.thenReturn(sDtoList.get(0), sDtoList.get(1), sDtoList.get(2));
		
		List<SkillDto> result = skillService.getAllSkills();
		
		assertThat(result).isNotNull();
		assertEquals(sList.size(), result.size());
		assertEquals(sDtoList.size(), result.size());
		assertEquals(sDtoList, result);
	}

	@Test
	void shouldGetSkillsAndReturnPaginatedDtos() {
		Page<Skill> p1 = new PageImpl<Skill>(sList.subList(0, 2));

		when(skillRepository.findAllByTitleContaining(any(String.class), any(Pageable.class))).thenReturn(p1);
		when(skillMapper.skillToSkillDto(any(Skill.class)))
			.thenReturn(sDtoList.get(0), sDtoList.get(1));
		
		List<SkillDto> result = skillService.getAllSkills(0, 2, "");
		
		assertThat(result).isNotNull();
		assertEquals(sList.subList(0, 2).size(), result.size());
	}
	
	@Test
	void shouldGetAllSkillsWithPageAndSizeLessThanZero() {
		Page<Skill> unpagedSkills = new PageImpl<Skill>(sList);

		when(skillRepository.findAllByTitleContaining(any(String.class), any(Pageable.class)))
			.thenReturn(unpagedSkills);
		when(skillMapper.skillToSkillDto(any(Skill.class)))
			.thenReturn(sDtoList.get(0), sDtoList.get(1));
		
		List<SkillDto> result = skillService.getAllSkills(0, 2, "");
		
		assertThat(result).isNotNull();
		assertEquals(sList.size(), result.size());
	}

	@Test
	void shouldReturnCountOfSkillsWithGivenTitle() {
		when(skillRepository.countByTitleContaining(any(String.class))).thenReturn((long)sList.size());
		
		CountDto result = skillService.count("");
		
		assertThat(result).isNotNull();
		assertEquals(sList.size(), result.getNb());
	}

	@Test
	void shouldGetSkillById() {
		when(skillRepository.findById(any(long.class))).thenReturn(Optional.of(sList.get(1)));
		when(skillMapper.skillToSkillDto(any(Skill.class))).thenReturn(sDtoList.get(1));
		
		SkillDto result = skillService.getById(2);
		
		assertThat(result).isNotNull();
		assertEquals(sDtoList.get(1), result);
	}
	
	@Test
	void shouldReturnNullWhenSkillIdIsUnknown() {
		when(skillRepository.findById(any(long.class))).thenReturn(Optional.empty());

		SkillDto result = skillService.getById(2222);

		assertThat(result).isNull();
	}

	@Test
	void shouldSaveNewSkill() {
		SkillDto toCreate = new SkillDto(0, "Java Expert", null, 0);
		Skill repoReturn = new Skill(3, "Java Expert", null, 0);
		SkillDto expected = new SkillDto(0, "Java Expert", null, 0);

		when(skillMapper.skillDtoToSkill(any(SkillDto.class)))
				.thenReturn(repoReturn);
		when(skillRepository.saveAndFlush(any(Skill.class))).thenReturn(repoReturn);
		when(skillMapper.skillToSkillDto(any(Skill.class))).thenReturn(expected);

		SkillDto result = skillService.saveOrUpdate(toCreate);

		assertThat(result).isNotNull();
		assertEquals(expected, result);
	}
	
	@Test
	void ShouldReturnNullWhenUpdateSkillWithWrongId() {
		SkillDto toUpdate = new SkillDto(1000, "Java Expert", null, 0);

		when(skillRepository.findById(any(long.class))).thenReturn(Optional.empty());

		SkillDto result = skillService.saveOrUpdate(toUpdate);

		assertThat(result).isNull();
	}

	@Test
	void shouldReturnTrueWhenSkillIntegrityIsOkay() {
		
		SkillDto badSkill = new SkillDto(1000, "Java Expert", userList, 0);
		
		when(userRepository.findById(any(long.class)))
			.thenReturn(Optional.of(Mockito.mock(User.class)));
		
		boolean result = skillService.checkIntegrity(badSkill);
		
		assertThat(result).isTrue();
	}

	@Test
	void shouldThrowErrorWhenSkillIntegrityIsBad() {

		SkillDto badSkill = new SkillDto(1000, "Java Expert", userList, 0);
		
		when(userRepository.findById(any(long.class))).thenReturn(Optional.empty());
		
		assertThrows(EntityFormatException.class, () -> {
			skillService.checkIntegrity(badSkill);
		});
	}

}
