package fr.dawan.calendarproject.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
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

	@Autowired
	RestTemplate restTemplate;
	
	/**
	 * Fetches all of the existing locations.
	 * 
	 * @return result Returns a list of locations.
	 *
	 */

	@Override
	public List<LocationDto> getAllLocations() {
		List<Location> locations = locationRepository.findAll();

		List<LocationDto> result = new ArrayList<>();
		for (Location l : locations) {
			result.add(locationMapper.locationToLocationDto(l));
		}
		return result;
	}
	
	/**
	 * Fetches all of the existing locations, with a pagination system.
	 * 
	 * @param page An integer representing the current page displaying the locations.
	 * @param size An integer defining the number of locations displayed by page.
	 * @param search A String representing the admin's input to search for a specific location.
	 * 
	 * @return result Returns a list of locations, according to the pagination criteria.
	 *
	 */

	@Override
	public List<LocationDto> getAllLocations(int page, int size, String search) {
		Pageable pagination = null;

		if (page > -1 && size > 0)
			pagination = PageRequest.of(page, size);
		else
			pagination = Pageable.unpaged();

		List<Location> locations = locationRepository.findAllByCityContaining(search, pagination).get()
				.collect(Collectors.toList());

		List<LocationDto> result = new ArrayList<>();
		for (Location l : locations) {
			result.add(locationMapper.locationToLocationDto(l));
		}
		return result;
	}
	
	/**
	 * Counts the number of locations.
	 * 
	 * @param search A String representing the admin's input to search for a specific location.
	 * 
	 * @return CountDto Returns the number of locations, according to the search criteria.
	 *
	 */

	@Override
	public CountDto count(String search) {
		return new CountDto(locationRepository.countByCityContaining(search));
	}
	
	/**
	 * Fetches a single location, according to their id.
	 * 
	 * @param id An unique Integer used to identify each location.
	 * 
	 * @return LocationDto Returns a single location.
	 *
	 */

	@Override
	public LocationDto getById(long id) {
		Optional<Location> l = locationRepository.findById(id);
		if (l.isPresent())
			return locationMapper.locationToLocationDto(l.get());
		return null;
	}
	
	/**
	 * Delete a single location, according to their id.
	 * 
	 * @param id An unique Integer used to identify each location.
	 *
	 */

	@Override
	public void deleteById(long id) {
		locationRepository.deleteById(id);
	}
	
	/**
	 * Adds a new location or updates an existing one.
	 * 
	 * @param locationDto An object representing an Location.
	 * 
	 * @return LocationDto Returns the newly created user or an updated one.
	 *
	 */

	@Override
	public LocationDto saveOrUpdate(LocationDto locationDto) {
		checkUniqness(locationDto);

		if (locationDto.getId() > 0 && !locationRepository.existsById(locationDto.getId()))
			return null;
		Location l = locationMapper.locationDtoToLocation(locationDto);

		l = locationRepository.saveAndFlush(l);
		return locationMapper.locationToLocationDto(l);
	}
	
	/**
	 * Checks whether a newly registered location is valid.
	 * 
	 * @param l An object representing an Location.
	 * 
	 * @return boolean Returns a boolean to say whether or not the location is correct.
	 *
	 */

	@Override
	public boolean checkUniqness(LocationDto location) {
		List<Location> duplicates = locationRepository.findDuplicates(location.getId(), location.getCity(),
				location.getColor());

		if (!duplicates.isEmpty()) {
			Set<APIError> errors = new HashSet<>();
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

	/**
	 * Fetch courses list from the webservice DG2
	 */
	@Override
	public void fetchAllDG2Locations(String email, String password) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		List<LocationDG2Dto> lResJson;

		URI url = new URI("https://dawan.org/api2/planning/locations");

		HttpHeaders headers = new HttpHeaders();
		headers.add("x-auth-token", email + ":" + password);

		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		ResponseEntity<String> repWs = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

		if (repWs.getStatusCode() == HttpStatus.OK) {
			String json = repWs.getBody();

			lResJson = objectMapper.readValue(json, new TypeReference<List<LocationDG2Dto>>() {
			});

			for (LocationDG2Dto lDG2 : lResJson) {
				Location locationImport = locationMapper.locationDG2DtoToLocation(lDG2);
				Location location;
				Optional<Location> optLocation = locationRepository.findById(locationImport.getId());

				if (optLocation.isPresent() && optLocation.get().equals(locationImport)) {
					location = locationImport;
					locationRepository.saveAndFlush(location);
				} else {
					location = locationImport;
					location.setColor("#00cc99");
					locationRepository.saveAndFlush(location);
				}
			}
		} else {
			throw new Exception("ResponseEntity from the webservice WDG2 not correct");
		}
	}
}
