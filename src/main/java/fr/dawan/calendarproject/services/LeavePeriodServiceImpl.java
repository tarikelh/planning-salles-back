package fr.dawan.calendarproject.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dawan.calendarproject.dto.LeavePeriodDg2Dto;
import fr.dawan.calendarproject.dto.LeavePeriodDto;
import fr.dawan.calendarproject.entities.LeavePeriod;
import fr.dawan.calendarproject.mapper.LeavePeriodMapper;
import fr.dawan.calendarproject.repositories.LeavePeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LeavePeriodServiceImpl implements LeavePeriodService {

    @Autowired
    private LeavePeriodRepository leavePeriodRepository;

    @Autowired
    private LeavePeriodMapper leavePeriodMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<LeavePeriodDto> getAllLeavePeriods() {
        List<LeavePeriod> leavePeriod = leavePeriodRepository.findAll();

        List<LeavePeriodDto> result = new ArrayList<>();

        for (LeavePeriod lp: leavePeriod) {
            result.add(leavePeriodMapper.leavePeriodToLeavePeriodDto(lp));
        }
        return result;
    }

    @Override
    public List<LeavePeriodDto> getAllLeavePeriodsByEmployeeId(long id) {
        Optional<List<LeavePeriod>> leavePeriod = leavePeriodRepository.findByUserEmployeeId(id);

        List<LeavePeriodDto> result = new ArrayList<>();
        if (leavePeriod.isPresent()) {
            for (LeavePeriod lp : leavePeriod.get()) {
                result.add(leavePeriodMapper.leavePeriodToLeavePeriodDto(lp));
            }
        }


        return result;
    }

    @Override
    public void fetchAllDG2LeavePeriods(String email, String password, String firstDay, String lastDay ) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<LeavePeriodDg2Dto> lResJson;

        String uri = "https://dawan.org/api2/planning/leave-periods/" + firstDay + "/" + lastDay;
        URI url = new URI(uri);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-AUTH-TOKEN", email + ":" + password);

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> repWs = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

        if (repWs.getStatusCode() == HttpStatus.OK) {
            String json = repWs.getBody();
            LeavePeriodDg2Dto[] resArray = objectMapper.readValue(json, LeavePeriodDg2Dto[].class);
            lResJson = Arrays.asList(resArray);
            for (LeavePeriodDg2Dto lpDg2 : lResJson) {

                LeavePeriod leavePeriodImport = leavePeriodMapper.leavePeriodDg2DtoToLeavePeriod(lpDg2);
                Optional<List<LeavePeriod>> optLeavePeriod = leavePeriodRepository.findByUserEmployeeId(leavePeriodImport.getEmployeeId());

                if ( !optLeavePeriod.isPresent() || !optLeavePeriod.get().contains(leavePeriodImport)) {
                    try {
                        leavePeriodRepository.saveAndFlush(leavePeriodImport);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
