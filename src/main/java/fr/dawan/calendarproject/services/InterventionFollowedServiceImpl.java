package fr.dawan.calendarproject.services;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.InterventionFollowedDG2Dto;
import fr.dawan.calendarproject.dto.InterventionFollowedDto;
import fr.dawan.calendarproject.entities.Intervention;
import fr.dawan.calendarproject.entities.InterventionFollowed;
import fr.dawan.calendarproject.entities.LeavePeriod;
import fr.dawan.calendarproject.entities.User;
import fr.dawan.calendarproject.enums.UserType;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.mapper.InterventionFollowedMapper;
import fr.dawan.calendarproject.repositories.InterventionFollowedRepository;
import fr.dawan.calendarproject.repositories.InterventionRepository;
import fr.dawan.calendarproject.repositories.LeavePeriodRepository;
import fr.dawan.calendarproject.repositories.UserRepository;

@Service
@Transactional
public class InterventionFollowedServiceImpl implements InterventionFollowedService {

    @Autowired
    private InterventionFollowedRepository intervFolloRepository;

    @Autowired
    private InterventionFollowedMapper intervFolloMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private LeavePeriodRepository leavePeriodRepository;
    
    @Autowired
    RestTemplate restTemplate;

    /**
     * Fetches all of the existing interventions Followed.
     * 
     * @return InterventionsFollowedDto Returns a list of interventions Followed.
     *
     */
    @Override
    public List<InterventionFollowedDto> getAllInterventionsFollowed() {

        return intervFolloMapper.listInterventionFollowedToListInterventionFollowedDto(intervFolloRepository.findAll());
    }

    /**
     * Fetches all of the existing interventions Followed, with a pagination system.
     * 
     * @param page An integer representing the current page displaying the
     *             interventions Followed.
     * @param max  An integer representing the last page of the pagination results.
     * 
     * @return interventionsFollowedDto Returns a list of interventions Followed,
     *         according to the
     *         pagination criteria.
     *
     */
    @Override
    public List<InterventionFollowedDto> getAllInterventionsFollowed(int page, int max) {
        List<InterventionFollowed> interventionsFoll = intervFolloRepository.findAll(PageRequest.of(page, max)).get()
                .collect(Collectors.toList());
        return intervFolloMapper.listInterventionFollowedToListInterventionFollowedDto(interventionsFoll);
    }

    /**
     * Counts the number of interventions Followed involving to a specific type of
     * user.
     * 
     * @param type A String referring to a type of user.
     * 
     * @return CountDto Returns the number of interventions, according to the search
     *         criteria.
     *
     */
    @Override
    public CountDto count(String type) {
        CountDto countDto = new CountDto();
        if (UserType.contains(type)) {
            countDto.setNb(intervFolloRepository.findByUserType(UserType.valueOf(type)).size());
        }
        return countDto;
    }

    /**
     * Fetches a single intervention Followed, according to its id.
     * 
     * @param id An unique Integer used to identify each intervention Followed.
     * 
     * @return InterventionFollowedDto Returns a single intervention Followed.
     *
     */
    @Override
    public InterventionFollowedDto getById(long id) {
        return intervFolloMapper
                .interventionFollowedToInterventionFollowedDto(intervFolloRepository.findById(id).orElse(null));
    }

    /**
     * Delete a single intervention Followed, according to its id.
     * 
     * @param id An unique Integer used to identify each intervention Followed.
     *
     */
    @Override
    public void deleteById(long id) {
        intervFolloRepository.deleteById(id);

    }

    /**
     * Adds a new intervention Followed or update an existing one.
     * 
     * @param interventionFollowed An object representing an Intervention.
     * 
     * @return InterventionFollowedDto Returns the newly created Intervention or an
     *         updated
     *         one.
     * @throws Exception 
     *
     */
    @Override
    public InterventionFollowedDto saveOrUpdate(InterventionFollowedDto interventionsFollowedDto) throws Exception {

        InterventionFollowedDto intervFolloDto = null;

        if (interventionsFollowedDto.getId() <= 0
                || intervFolloRepository.findById(interventionsFollowedDto.getId()).isPresent()) {
            

            
            InterventionFollowed i = intervFolloMapper
                    .interventionFollowedDtoToInterventionFollowed(interventionsFollowedDto);
            User user = userRepository.findById(interventionsFollowedDto.getUserId()).orElse(null);
            Intervention intervention = interventionRepository.findById(interventionsFollowedDto.getInterventionId())
                    .orElse(null);
            if (user != null) {
                i.setUser(user);
            } else {
                Set<APIError> errors = new HashSet<>();
                errors.add(new APIError(404, "User", "User not found",
                        "User with id " + interventionsFollowedDto.getUserId() + " not found", "/api/users"));

                throw new EntityFormatException(errors);
            }
            if (intervention != null) {
                i.setIntervention(intervention);
            } else {
                Set<APIError> errors = new HashSet<>();
                errors.add(new APIError(404, "Intervention", "Intervention not found",
                        "Intervention with id " + interventionsFollowedDto.getInterventionId() + " not found",
                        "/api/interventions"));

                throw new EntityFormatException(errors);
            }
            checkValidity(user.getId(), intervention);
            if (i.getRegistrationSlug() == null || i.getRegistrationSlug().equals("")) {
                i.setRegistrationSlug(createInterventionFollowedRegistrationSlug(i));
            }
            
            try {
                i = intervFolloRepository.saveAndFlush(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
            intervFolloDto = intervFolloMapper.interventionFollowedToInterventionFollowedDto(i);
        }
        return intervFolloDto;
    }

    /**
     * Checks whether a newly registered intervention is valid.
     * 
     * @param iFollowed An object representing an InterventionFollowed.
     * 
     * @return boolean Returns a boolean to say whether or not the
     *         interventionFollowed is
     *         correct.
     *
     * @throws Exception 
     */

    public void checkValidity(long userId, Intervention intervention) throws Exception {
        Set<APIError> errors = new HashSet<>();
        
        String errorType = "DateOverlap";
        String apiRoute = "/api/interventionsFollowed";

        List<Intervention> intervs = interventionRepository.findInterventionsOverlapingByUserIdAndDates(userId, intervention.getDateStart(), intervention.getDateEnd());
        if (!intervs.isEmpty())  {
        	String message = "InterventionFollowed dates overlap the intervention with id: "
        			+ intervs.get(0).getId() + ".";
        	errors.add(new APIError(404, "intervention", errorType, message, apiRoute));
        }
   
        
        List<InterventionFollowed> intervsFollow = intervFolloRepository.getAllByUserIdAndDateRange(userId, intervention.getDateStart(), intervention.getDateEnd());
            
        if (!intervsFollow.isEmpty())  {
        	String message = "InterventionFollowed dates overlap the interventionFollowed with id: "
        			+ intervsFollow.get(0).getId() + ".";
        	errors.add(new APIError(404, "interventionFollowed", errorType, message, apiRoute));
        }
        
        List<LeavePeriod> leavePeriods = leavePeriodRepository.getAllOverlapingByUserIdAndDates(userId, intervention.getDateStart(), intervention.getDateEnd());

        if(!leavePeriods.isEmpty()) {
        	String message = "InterventionFollowed dates overlap the Leave period with id: "
        			+ leavePeriods.get(0).getId() + ".";
        	errors.add(new APIError(404, "leavePeriod", errorType, message, apiRoute));
        }

        if (!errors.isEmpty()){
        	throw new EntityFormatException(errors);
        }
    }

    /**
     * Fetches all of the existing interventions Followed with a specific user type.
     * 
     * @param type A UserType referring to the type of user.
     * 
     * @return Returns a list of InterventionFollowedDto according to the user type.
     *
     */
    @Override
    public List<InterventionFollowedDto> findAllByUserType(String type) {
        UserType userType = Enum.valueOf(UserType.class, type);
        return intervFolloMapper
                .listInterventionFollowedToListInterventionFollowedDto(intervFolloRepository.findByUserType(userType));
    }

    /**
     * Fetches all of the existing interventions Followed between two dates and with
     * a specific user type.
     * 
     * @param type  A UserType referring to the type of user.
     * @param start A LocalDate referring to the starting date.
     * @param end   A LocalDate referring to the end date.
     * 
     * @return Returns a list of InterventionFollowedDto.
     *
     */
    @Override
    public List<InterventionFollowedDto> findAllByUserTypeAndDateRange(String type, LocalDate start, LocalDate end) {
        UserType userType = Enum.valueOf(UserType.class, type);
        List<InterventionFollowed> lstIntervFollo = intervFolloRepository.getAllByUserTypeAndDateRange(userType, start,
                end);
        return intervFolloMapper.listInterventionFollowedToListInterventionFollowedDto(lstIntervFollo);
    }

    /**
     * Fetches all of the existing interventions Followed between two dates.
     * 
     * @param start A LocalDate referring to the starting date.
     * @param end   A LocalDate referring to the end date.
     * 
     * @return Returns a list of InterventionFollowedDto.
     *
     */
    @Override
    public List<InterventionFollowedDto> findAllByDateRange(LocalDate start, LocalDate end) {
        return intervFolloMapper.listInterventionFollowedToListInterventionFollowedDto(
                intervFolloRepository.findAllByDateRange(start, end));
    }

    /**
     * Creates a unique registrationSlug for InterventionFollowed
     * 
     * @param title     : the title of the task we want to create a Slug for
     * @param dateStart : the date of beggining of the task
     * @return the constructed slug as String
     */
    private String createInterventionFollowedRegistrationSlug(InterventionFollowed intervFollowed) {
        StringBuilder registrationSlug = new StringBuilder();
        List<InterventionFollowed> intervsFollowed;
        String lastName = "";
        String firstName = "";

        if (intervFollowed != null) {
            lastName = intervFollowed.getLastName();
            firstName = intervFollowed.getFirstName();
            registrationSlug.append(lastName).append("-").append(firstName).append("-");

            intervsFollowed = intervFolloRepository
                    .findAllByRegistrationSlugContaining(registrationSlug.toString());

            if (!intervsFollowed.isEmpty()) {
                registrationSlug.append("1--").append(lastName).append("-")
                        .append(firstName).append("-1-").append(intervsFollowed.size() + 1);

            } else {
                registrationSlug.append("-");
            }

        }
        return registrationSlug.toString();
    }

    /**
     * Counts all interventions Followed in the Dawan API, between two specific
     * dates.
     * 
     * @param email A String defining a user's email.
     * @param pwd   A String defining a user's password.
     * @param start A LocalDate referring to the starting date of an intervention.
     * @param end   A LocalDate referring to the end date of intervention.
     * 
     * @return count Returns the number of interventions of the Dawan API between
     *         two dates.
     * 
     * @exception Exception Returns an exception if the request fails.
     *
     */
    @Override
    public int fetchAllDG2InterventionsFollowed(String email, String pwd, LocalDate start, LocalDate end)
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<InterventionFollowedDG2Dto> lResJson;
        List<InterventionFollowed> intervsFollToSave = new ArrayList<>();
        int count = 0;

        URI url = new URI(String.format("https://dawan.org/api2/planning/trainings-followed/by-date/" + start.toString()
                + "/" + end.toString()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-AUTH-TOKEN", email + ":" + pwd);

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> repWs = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

        if (repWs.getStatusCode() == HttpStatus.OK) {
            String json = repWs.getBody();
            InterventionFollowedDG2Dto[] resArray = objectMapper.readValue(json, InterventionFollowedDG2Dto[].class);

            lResJson = Arrays.asList(resArray);

            for (InterventionFollowedDG2Dto iFDG2 : lResJson) {
                InterventionFollowed intervsFoll = intervFolloMapper
                        .interventionFollowedDG2DtoToInterventionFollowed(iFDG2);
                intervsFoll.setIntervention(
                        interventionRepository.findByIdDg2WithOutOption(iFDG2.getInterventionId()).orElse(null));
                intervsFoll.setUser(userRepository.findByIdDg2(iFDG2.getPersonId()).orElse(null));

                if (intervsFoll != null && intervsFoll.getIntervention() != null && intervsFoll.getUser() != null) {

                    InterventionFollowed alreadyInDb = intervFolloRepository
                            .findByRegistrationSlug(iFDG2.getRegistrationSlug()).orElse(null);

                    if (alreadyInDb != null && !alreadyInDb.equals(intervsFoll)) {

                        intervsFoll.setId(alreadyInDb.getId());
                        intervsFoll.setVersion(alreadyInDb.getVersion());
                        intervsFollToSave.add(intervsFoll);
                        count++;

                    } else if (alreadyInDb == null) {
                        intervsFollToSave.add(intervsFoll);
                        count++;
                    }

                }

            }
        } else {
            throw new Exception("ResponseEntity from the webservice WDG2 not correct");
        }

        try {
            intervsFollToSave = intervFolloRepository.saveAll(intervsFollToSave);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;

    }

}
