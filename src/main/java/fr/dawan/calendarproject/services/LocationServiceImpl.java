package fr.dawan.calendarproject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fr.dawan.calendarproject.dto.DtoTools;
import fr.dawan.calendarproject.dto.LocationDto;
import fr.dawan.calendarproject.entities.Location;
import fr.dawan.calendarproject.repositories.LocationRepository;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {

	@Autowired
	LocationRepository locationRepository;

	@Override
	public List<LocationDto> getAllLocations() {
		List<Location> locations = locationRepository.findAll();

		// Solution 1 à la mano - conversion vers Dto
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
		Location l = DtoTools.convert(locationDto, Location.class);
		if(l.getId() != 0) {
			l.setVersion(locationRepository.getOne(l.getId()).getVersion());
		}
		l = locationRepository.saveAndFlush(l);
		return DtoTools.convert(l, LocationDto.class);
	}

	@Override
	public LocationDto count() {
		// TODO Auto-generated method stub
		return null;
	}

}
