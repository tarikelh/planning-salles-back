package fr.dawan.calendarproject.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.DateRangeDto;
import fr.dawan.calendarproject.dto.InterventionDto;
import fr.dawan.calendarproject.dto.MailingListDto;
import fr.dawan.calendarproject.interceptors.TokenInterceptor;
import fr.dawan.calendarproject.services.EmailService;
import fr.dawan.calendarproject.services.InterventionService;
import fr.dawan.calendarproject.tools.ICalTools;
import fr.dawan.calendarproject.tools.JwtTokenUtil;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.property.XProperty;

@SpringBootTest
@AutoConfigureMockMvc
class InterventionControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private InterventionController interventionController;

	@MockBean
	private ICalTools iCalTools;

	@MockBean
	private InterventionService interventionService;

	@MockBean
	private TokenInterceptor tokenInterceptor;

	@MockBean
	private JwtTokenUtil jwtTokenUtil;

	@MockBean
	private HttpServletRequest request;

	@MockBean
	private EmailService emailService;

	private List<InterventionDto> intervs = new ArrayList<InterventionDto>();

	@BeforeEach()
	public void beforeEach() throws Exception {
		when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn("test@testEmail.com");

		intervs.add(new InterventionDto(1, 1, "slug-1", "commentaire id 1", 1, 1, 1, 1, 1, 0, "SUR_MESURE", true,
				LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, false, 0));
		intervs.add(new InterventionDto(2, 2, "slug-2", "commentaire id 2", 2, 2, 2, 2, 2, 0, "INTERN", true, LocalDate.now(),
				LocalDate.now().plusDays(2), LocalTime.of(9, 0), LocalTime.of(17, 0), 0, true, 0));
		intervs.add(new InterventionDto(3, 3, "slug-3", "commentaire id 3", 3, 3, 3, 3, 3, 0, "INTERN", true,
				LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0), LocalTime.of(17, 0), 2,
				false, 0));
	}

	@Test
	void contextLoads() {
		assertThat(interventionController).isNotNull();
	}

	@Test
	void shouldFetchInterventions() throws Exception {
		when(interventionService.getAllInterventions()).thenReturn(intervs);

		mockMvc.perform(get("/api/interventions").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(intervs.size())))
				.andExpect(jsonPath("$[0].comment", is(intervs.get(0).getComment())))
				.andExpect(jsonPath("$[0].courseId", is(1))).andExpect(jsonPath("$[0].userId", is(1)))
				.andExpect(jsonPath("$[0].locationId", is(1)));
	}

	@Test
	void shouldFetchById() throws Exception {
		final long intervId = 2;
		when(interventionService.getById(intervId)).thenReturn(intervs.get(1));

		mockMvc.perform(get("/api/interventions/{id}", intervId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.comment", is(intervs.get(1).getComment())))
				.andExpect(jsonPath("$.courseId", is(2))).andExpect(jsonPath("$.userId", is(2)))
				.andExpect(jsonPath("$.locationId", is(2)));
	}

	@Test
	void shouldFetchByUserId() throws Exception {
		final long userId = 3;
		when(interventionService.getAllByUserId(userId)).thenReturn(intervs.subList(2, 3));

		mockMvc.perform(get("/api/interventions/user/{userId}", userId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(1)));
	}

	@Test
	void shouldFetchByUserIdAndFilters() throws Exception {
		final long userId = 3;
		when(interventionService.searchBy(any(long.class), Mockito.anyMap())).thenReturn(intervs.subList(2, 3));

		mockMvc.perform(get("/api/interventions/filter/{userId}", userId, request).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(1)));
	}

	@Test
	void shouldGetMasterIntervention() throws Exception {
		List<InterventionDto> masterIntervs = new ArrayList<InterventionDto>();
		masterIntervs.add(intervs.get(1));

		when(interventionService.getMasterIntervention()).thenReturn(masterIntervs);

		mockMvc.perform(get("/api/interventions/masters").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(masterIntervs.size())))
				.andExpect(jsonPath("$[0].comment", is(masterIntervs.get(0).getComment())))
				.andExpect(jsonPath("$[0].courseId", is(2))).andExpect(jsonPath("$[0].userId", is(2)))
				.andExpect(jsonPath("$[0].locationId", is(2)));
	}

	@Test
	void shouldGetMasterInterventionById() throws Exception {
		final long id = 2;
		InterventionDto masterInterv = new InterventionDto(1, 1, "slug-1", "I am lambda Intervention", 0, 0, 0, 0, 0, 0,
				"SUR_MESURE", true, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0),
				LocalTime.of(17, 0), 0, true, 0);

		when(interventionService.getById(id)).thenReturn(masterInterv);

		mockMvc.perform(get("/api/interventions/masters/{id}", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.comment", is(masterInterv.getComment())));
	}

	@Test
	void shouldThrowErrorWhenInterventionNotMaster() throws Exception {
		final long id = 2;
		InterventionDto masterInterv = new InterventionDto(1, 1, "slug-1", "I am lambda Intervention", 0, 0, 0, 0, 0, 0,
				"SUR_MESURE", true, LocalDate.now(), LocalDate.now().plusDays(5), LocalTime.of(9, 0),
				LocalTime.of(17, 0), 0, false, 0);

		when(interventionService.getById(id)).thenReturn(masterInterv);

		mockMvc.perform(get("/api/interventions/masters/{id}", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldGetSubInterventions() throws Exception {
		List<InterventionDto> subIntervs = new ArrayList<InterventionDto>();
		subIntervs.add(intervs.get(2));

		when(interventionService.getSubInterventions(any(String.class), any(LocalDate.class), any(LocalDate.class)))
				.thenReturn(subIntervs);

		mockMvc.perform(get("/api/interventions/sub?type=test&start=2021-09-01&end=2021-09-05")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(subIntervs.size())))
				.andExpect(jsonPath("$[0].comment", is(subIntervs.get(0).getComment())))
				.andExpect(jsonPath("$[0].courseId", is(3))).andExpect(jsonPath("$[0].userId", is(3)))
				.andExpect(jsonPath("$[0].locationId", is(3)));
	}

	@Test
	void shouldGetSubInterventionsWithMasterId() throws Exception {
		final long id = 2;
		List<InterventionDto> subIntervs = new ArrayList<InterventionDto>();
		subIntervs.add(intervs.get(2));

		when(interventionService.getSubByMasterId(id)).thenReturn(subIntervs);

		mockMvc.perform(get("/api/interventions/sub/{masterId}", id).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(subIntervs.size())))
				.andExpect(jsonPath("$[0].comment", is(subIntervs.get(0).getComment())))
				.andExpect(jsonPath("$[0].courseId", is(3))).andExpect(jsonPath("$[0].userId", is(3)))
				.andExpect(jsonPath("$[0].locationId", is(3)));
	}

	@Test
	void shouldDeleteById() throws Exception {
		final long intId = 1;
		doNothing().when(interventionService).deleteById(any(Long.class), any(String.class));

		String res = mockMvc
				.perform(delete("/api/interventions/{id}", intId).accept(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer testTokenForTestingPurpose"))
				.andExpect(status().isAccepted()).andReturn().getResponse().getContentAsString();

		assertEquals(res, "Intervention with id " + intId + " Deleted");
	}

	void shouldReturn404DeleteWithWrongId() throws Exception {
		final long intId = 105;
		doThrow(IllegalArgumentException.class).when(interventionService).deleteById(any(Long.class),
				any(String.class));

		String res = mockMvc.perform(delete("/api/interventions/{id}", intId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted()).andReturn().getResponse().getContentAsString();

		assertEquals(res, "Intervention with id " + intId + " Not Found");
	}

	@Test
	void shouldCreateNewIntervention() throws Exception {
		InterventionDto interv = new InterventionDto(0, 0, "slug-4", "commentaire id 4", 4, 4, 4, 4, 4, 0, "INTERN", true,
				LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0), LocalTime.of(17, 0), 0,
				false, 0);
		InterventionDto result = new InterventionDto(4, 4, "slug-4", "commentaire id 4", 4, 4, 4, 4, 4, 0, "INTERN", true,
				LocalDate.now().plusDays(7), LocalDate.now().plusDays(10), LocalTime.of(9, 0), LocalTime.of(17, 0), 0,
				false, 0);

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String intervJson = objectMapper.writeValueAsString(interv);

		when(interventionService.saveOrUpdate(any(InterventionDto.class), any(String.class))).thenReturn(result);

		mockMvc.perform(
				post("/api/interventions").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer testTokenForTestingPurpose").content(intervJson))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(4))).andExpect(jsonPath("$.courseId", is(4)))
				.andExpect(jsonPath("$.userId", is(4))).andExpect(jsonPath("$.locationId", is(4)));
	}

	@Test
	void shouldUpdateIntervention() throws Exception {
		InterventionDto updatedInterv = intervs.get(0);

		String oldComment = updatedInterv.getComment();
		String newComment = "This is a new Comment";

		updatedInterv.setComment(newComment);

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String intervJson = objectMapper.writeValueAsString(updatedInterv);

		when(interventionService.saveOrUpdate(any(InterventionDto.class), any(String.class))).thenReturn(updatedInterv);

		mockMvc.perform(
				put("/api/interventions").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer testTokenForTestingPurpose").content(intervJson))
				.andExpect(status().isOk()).andExpect(jsonPath("$.comment", is(newComment)))
				.andExpect(jsonPath("$.comment", not(oldComment)));
	}

	@Test
	void shouldReturn404WhenUpdateWrongId() throws Exception {
		InterventionDto updatedInterv = intervs.get(0);

		updatedInterv.setId(1212121);

		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String intervJson = objectMapper.writeValueAsString(updatedInterv);

		when(interventionService.saveOrUpdate(any(InterventionDto.class), any(String.class))).thenReturn(null);

		String res = mockMvc
				.perform(put("/api/interventions").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer testTokenForTestingPurpose").content(intervJson))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

		assertEquals(res, "Intervention with Id " + updatedInterv.getId() + " Not Found");
	}

	@Test
	void shouldExportInteventionsAsIcal() throws Exception {
		final long userId = 1;

		Calendar cal = new Calendar();
		cal.getProperties().add(new XProperty("X-CALNAME", "test"));

		File f = new File("test.ics");
		f.createNewFile();

		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(f.getAbsolutePath())));

		when(interventionService.exportCalendarAsICal(userId)).thenReturn(cal);
		MockedStatic<ICalTools> calTools = Mockito.mockStatic(ICalTools.class);
		calTools.when(() -> ICalTools.generateICSFile(any(Calendar.class), any(String.class), any(File.class)))
				.thenReturn(resource);

		MockHttpServletResponse response = mockMvc
				.perform(get("/api/interventions/ical/{userId}", userId).contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
				.andReturn().getResponse();

		assertEquals("attachment; filename=test.ics", response.getHeader("content-disposition"));
		assertEquals("no-cache, no-store, must-revalidate", response.getHeader("Cache-Control"));
		assertEquals("no-cache", response.getHeader("pragma"));
		assertEquals("0", response.getHeader("Expires"));

		f.delete();
		if (!calTools.isClosed())
			calTools.close();
	}

	@Test
	void shoudReturnErrorWhenExportFails() throws Exception {
		final long userId = 1;
		Calendar cal = new Calendar();
		cal.getProperties().add(new XProperty("X-CALNAME", "test"));
		File f = Mockito.mock(File.class);
		when(interventionService.exportCalendarAsICal(userId)).thenReturn(cal);
		when(f.getAbsolutePath()).thenReturn("test.ics");

		MockedStatic<ICalTools> calTools = Mockito.mockStatic(ICalTools.class);
		calTools.when(() -> ICalTools.generateICSFile(any(Calendar.class), any(String.class), any(File.class)))
				.thenThrow(IOException.class);

		mockMvc.perform(get("/api/interventions/ical/{userId}", userId).contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isInternalServerError());

		if (!calTools.isClosed())
			calTools.close();
	}

	@Test
	void shouldReturn404WhenUserIsNotFoundOrHasNoIntervention() throws Exception {
		final long userId = 1;

		when(interventionService.exportCalendarAsICal(userId)).thenReturn(null);

		mockMvc.perform(get("/api/interventions/ical/{userId}", userId).contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldReturnInterventionCountByUserType() throws Exception {

		when(interventionService.count(any(String.class))).thenReturn(new CountDto(2));

		mockMvc.perform(get("/api/interventions/count?type=test").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.nb", is(2)));
	}

	@Test
	void shouldSendCalendarToEmployees() throws Exception {
		List<Long> userIdList = new ArrayList<Long>();
		userIdList.add(1L);
		MailingListDto mailList = new MailingListDto(userIdList, "2021-11-03", "2021-11-05");
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String mailListJson = objectMapper.writeValueAsString(mailList);

		doNothing().when(emailService).sendCalendarToSelectedEmployees(Mockito.anyList(), any(LocalDate.class),
				any(LocalDate.class));

		mockMvc.perform(post("/api/interventions/email/employees").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(mailListJson)).andExpect(status().isAccepted());
	}

	@Test
	void shouldSplitIntervention() throws Exception {
		final long interventionId = 1;
		List<DateRangeDto> dates = new ArrayList<DateRangeDto>();
		dates.add(new DateRangeDto(1, LocalDate.now(), LocalDate.now().plusDays(2), LocalTime.of(9, 0),
				LocalTime.of(17, 0)));
		dates.add(new DateRangeDto(0, LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), LocalTime.of(9, 0),
				LocalTime.of(17, 0)));
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		String datesJson = objectMapper.writeValueAsString(dates);

		when(interventionService.splitIntervention(any(long.class), Mockito.anyList()))
				.thenReturn(intervs.subList(0, 1));

		mockMvc.perform(post("/api/interventions/split/{id}", interventionId).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(datesJson)).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(1)));
	}

	@Test
	void shouldFetchInterventionFromDg2AndReturnOkStatus() throws Exception {
		LocalDate start = LocalDate.now();
		LocalDate end = LocalDate.now().plusDays(2);

		when(interventionService.fetchDG2Interventions(any(String.class), any(String.class), any(LocalDate.class),
				any(LocalDate.class))).thenReturn(3);

		mockMvc.perform(get("/api/interventions/dg2/{start}/{end}", start.toString(), end.toString())
				.accept(MediaType.APPLICATION_JSON).header("x-auth-token", "test@dawan.fr:testPassword"))
				.andExpect(status().isOk());

	}

	@Test
	void shouldReturnInternalServerErrorStatusIfFetchDg2Throw() throws Exception {
		LocalDate start = LocalDate.now();
		LocalDate end = LocalDate.now().plusDays(2);

		doThrow(Exception.class).when(interventionService).fetchDG2Interventions(any(String.class), any(String.class),
				any(LocalDate.class), any(LocalDate.class));

		mockMvc.perform(get("/api/interventions/dg2/{start}/{end}", start.toString(), end.toString())
				.accept(MediaType.APPLICATION_JSON).header("X-AUTH-TOKEN", "test@dawan.fr:testPassword"))
				.andExpect(status().isInternalServerError());
	}
}
