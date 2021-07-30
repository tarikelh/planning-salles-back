package fr.dawan.calendarproject.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.APIError;
import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.exceptions.EntityFormatException;
import fr.dawan.calendarproject.repositories.LocationRepository;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationRepository locationRepository;

	@Override
	public List<LocationDto> getAllLocations() {
		List<Location> locations = locationRepository.findAll();

		List<LocationDto> result = new ArrayList<LocationDto>();
		for (Location l : locations) {
			result.add(DtoTools.convert(l, LocationDto.class));
		}
		return result;
	}

	@Override
	public List<LocationDto> getAllLocations(int page, int max) {
		List<Location> locations = locationRepository.findAll(PageRequest.of(page, max)).get()
				.collect(Collectors.toList());
		List<LocationDto> result = new ArrayList<LocationDto>();
		for (Location l : locations) {
			result.add(DtoTools.convert(l, LocationDto.class));
		}
		return result;
	}

	@Override
	public LocationDto getById(long id) {
		Optional<Location> l = locationRepository.findById(id);
		if (l.isPresent())
			return DtoTools.convert(l.get(), LocationDto.class);
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
		Location l = DtoTools.convert(locationDto, Location.class);

		l = locationRepository.saveAndFlush(l);
		return DtoTools.convert(l, LocationDto.class);
	}

	@Override
	public LocationDto count() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkUniqness(LocationDto location) {
		System.out.println("id ::: " + location.getId());
		System.out.println("city ::: " + location.getCity());
		System.out.println("color ::: " + location.getColor());
		List<Location> duplicates = locationRepository.findDuplicates(location.getId(), location.getCity(), location.getColor());
		
		System.out.println("dup size ::: " + duplicates.size());
		if (duplicates.size() > 0) {
			Set<APIError> errors = new HashSet<APIError>();
			String instanceClass = location.getClass().toString();
			String path = "/api/locations";
			
			for (Location loc : duplicates) {
				if (loc.getCity().equals(location.getCity()))
					errors.add(new APIError(505, instanceClass, path, "Location with name " + location.getCity() + " already exists", path));
				if (loc.getColor().equals(location.getColor()))
					errors.add(new APIError(505, instanceClass, path, "Location with color " + location.getColor() + " already exists", path));
			}
			
			throw new EntityFormatException(errors);
		}
		
		return true;
	}

}
