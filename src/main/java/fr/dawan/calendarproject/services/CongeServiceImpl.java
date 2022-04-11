package fr.dawan.calendarproject.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dawan.calendarproject.dto.CongeDg2Dto;
import fr.dawan.calendarproject.dto.CongeDto;
import fr.dawan.calendarproject.entities.Conge;
import fr.dawan.calendarproject.mapper.CongeMapper;
import fr.dawan.calendarproject.repositories.CongeRepository;
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
public class CongeServiceImpl implements CongeService {

    @Autowired
    private CongeRepository congeRepository;

    @Autowired
    private CongeMapper congeMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<CongeDto> getAllConges() {
        List<Conge> conges = congeRepository.findAll();

        List<CongeDto> result = new ArrayList<>();

        for (Conge c: conges) {
            result.add(congeMapper.congeToCongeDto(c));
        }
        return result;
    }

    @Override
    public List<CongeDto> getAllCongesByEmployeeId(long id) {
        Optional<List<Conge>> conges = congeRepository.findByUserEmployeeId(id);

        List<CongeDto> result = new ArrayList<>();
        if (conges.isPresent()) {
            for (Conge c : conges.get()) {
                result.add(congeMapper.congeToCongeDto(c));
            }
        }


        return result;
    }

    @Override
    public void fetchAllDG2Conges(String email, String password) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CongeDg2Dto> lResJson;

        String uri = "https://dawan.org/api2/planning/leave-periods/2022-01-01/2022-12-31";
        URI url = new URI(uri);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-AUTH-TOKEN", email + ":" + password);

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> repWs = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

        if (repWs.getStatusCode() == HttpStatus.OK) {
            String json = repWs.getBody();
            CongeDg2Dto[] resArray = objectMapper.readValue(json, CongeDg2Dto[].class);
            lResJson = Arrays.asList(resArray);
            for (CongeDg2Dto cDg2 : lResJson) {
                Conge congeImport = congeMapper.congeDg2DtoToConge(cDg2);
                Optional<List<Conge>> optConge = congeRepository.findByUserEmployeeId(congeImport.getEmployeeId());

                if (!optConge.isPresent() || !optConge.get().equals(congeImport)) {
                    try {
                        congeRepository.saveAndFlush(congeImport);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
