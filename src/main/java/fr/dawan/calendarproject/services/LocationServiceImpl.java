package fr.dawan.calendarproject.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.dto.CountDto;
import fr.dawan.calendarproject.dto.LocationDG2Dto;
import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.mapper.LocationMapper;
import fr.dawan.calendarproject.repositories.LocationRepository;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private LocationMapper locationMapper;
	
	@Override
	public List<LocationDto> getAllLocations() {
		List<Location> locations = locationRepository.findAll();

		List<LocationDto> result = new ArrayList<LocationDto>();
		for (Location l : locations) {
			result.add(locationMapper.locationToLocationDto(l));
		}
		return result;
	}

	@Override
	public List<LocationDto> getAllLocations(int page, int size, String search) {
		Pageable pagination = null;
		
		if(page > -1 && size > 0) 
			pagination = PageRequest.of(page, size);
		else
			pagination = Pageable.unpaged();
		
		List<Location> locations = locationRepository.findAllByCityContaining(search, pagination).get().collect(Collectors.toList());

		List<LocationDto> result = new ArrayList<LocationDto>();
		for (Location l : locations) {
			result.add(locationMapper.locationToLocationDto(l));
		}
		return result;
	}
	

	@Override
	public CountDto count(String search) {
		return new CountDto(locationRepository.countByCityContaining(search));
	}

	
	@Override
	public LocationDto getById(long id) {
		Optional<Location> l = locationRepository.findById(id);
		if (l.isPresent())
			return locationMapper.locationToLocationDto(l.get());
		return null;
	}

	@Override
	public void deleteById(long id) {
		locationRepository.deleteById(id);
	}

	@Override
	public LocationDto saveOrUpdate(LocationDto locationDto) {
		checkUniqness(locationDto);

		if (locationDto.getId() > 0 && !locationRepository.existsById(locationDto.getId()))
			return null;
		Location l = locationMapper.locationDtoToLocation(locationDto);

		l = locationRepository.saveAndFlush(l);
		return locationMapper.locationToLocationDto(l);
	}

	@Override
	public boolean checkUniqness(LocationDto location) {
		List<Location> duplicates = locationRepository.findDuplicates(location.getId(), location.getCity(),
				location.getColor());

		if (duplicates.size() > 0) {
			Set<APIError> errors = new HashSet<APIError>();
			String instanceClass = duplicates.get(0).getClass().toString();
			String path = "/api/locations";

			for (Location loc : duplicates) {
				if (loc.getCity().equals(location.getCity()))
					errors.add(new APIError(505, instanceClass, "City Name not Unique",
							"Location with name " + location.getCity() + " already exists", path));
				if (loc.getColor().equals(location.getColor()))
					errors.add(new APIError(505, instanceClass, "Color not Unique",
							"Location with color " + location.getColor() + " already exists", path));
			}

			throw new EntityFormatException(errors);
		}

		return true;
	}

	@Override
	public void fetchAllDG2Locations() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		RestTemplate restTemplate = new RestTemplate();
		List<LocationDG2Dto> lResJson = new ArrayList<LocationDG2Dto>();
		
		URI url = new URI("https://dawan.org/public/location/");
		ResponseEntity<String> repWs = restTemplate.getForEntity(url, String.class);
		
		if(repWs.getStatusCode()==HttpStatus.OK) {
			String json = repWs.getBody();
			LocationDG2Dto[] resArray = objectMapper.readValue(json, LocationDG2Dto[].class);
			lResJson = Arrays.asList(resArray);
			for (LocationDG2Dto lDG2 : lResJson) {
				Location l = locationMapper.locationDG2DtoToLocation(lDG2);
				Location foundL = locationRepository.findByCity(l.getCity());
				if(foundL != null) {
					l.setId(foundL.getId());
					l.setColor(foundL.getColor());
				}
				locationRepository.saveAndFlush(l);
			}
		} else {
			 throw new Exception("ResponseEntity from the webservice WDG2 not correct");   
		}
	}

}
